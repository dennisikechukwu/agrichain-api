package com.agrichain.auth;

import com.agrichain.common.exception.DuplicateResourceException;
import com.agrichain.common.exception.ResourceNotFoundException;
import com.agrichain.farmer.Farmer;
import com.agrichain.farmer.FarmerRepository;
import com.agrichain.offtaker.OffTaker;
import com.agrichain.offtaker.OffTakerRepository;
import com.agrichain.user.Role;
import com.agrichain.user.User;
import com.agrichain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final OffTakerRepository offTakerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public AuthResponse registerFarmer(FarmerRegisterRequest request) {
        checkEmailNotTaken(request.email());

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.FARMER)
                .build();
        userRepository.save(user);

        Farmer farmer = Farmer.builder()
                .user(user)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .state(request.state())
                .lga(request.lga())
                .farmSize(request.farmSize())
                .build();
        farmerRepository.save(farmer);

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse registerOffTaker(OffTakerRegisterRequest request) {
        checkEmailNotTaken(request.email());

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.OFF_TAKER)
                .build();
        userRepository.save(user);

        OffTaker offTaker = OffTaker.builder()
                .user(user)
                .companyName(request.companyName())
                .contactPerson(request.contactPerson())
                .phone(request.phone())
                .build();
        offTakerRepository.save(offTaker);

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse registerAdmin(AdminRegisterRequest request) {
        checkEmailNotTaken(request.email());

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Throws automatically if credentials are wrong — no manual checks needed
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.refreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found. Please log in again."));

        refreshTokenService.verifyRefreshToken(refreshToken);

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);
        String newAccessToken = jwtService.generateToken(newRefreshToken.getUser());

        return new AuthResponse(newAccessToken, newRefreshToken.getToken(), newRefreshToken.getUser().getEmail(), newRefreshToken.getUser().getRole().name());
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.refreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found. Please log in again."));

        refreshTokenService.revokeAllUserTokens(refreshToken.getUser());
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void checkEmailNotTaken(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("An account with this email already exists");
        }
    }

    // Extracted so all three register methods share the same token generation logic
    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken.getToken(), user.getEmail(), user.getRole().name());
    }
}
