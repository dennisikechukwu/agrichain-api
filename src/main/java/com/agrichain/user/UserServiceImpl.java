package com.agrichain.user;

import com.agrichain.common.exception.ResourceNotFoundException;
import com.agrichain.farmer.Farmer;
import com.agrichain.farmer.FarmerRepository;
import com.agrichain.offtaker.OffTaker;
import com.agrichain.offtaker.OffTakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final OffTakerRepository offTakerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile() {
        // JwtAuthFilter already validated the token and stored the user here
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Build the profile response based on what role the user has
        return switch (user.getRole()) {
            case FARMER -> {
                Farmer farmer = farmerRepository.findByUser(user)
                        .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

                yield new UserProfileResponse(
                        user.getId(), user.getEmail(), user.getRole().name(), user.getCreatedAt(),
                        farmer.getFirstName(), farmer.getLastName(), farmer.getPhone(),
                        farmer.getState(), farmer.getLga(), farmer.getFarmSize(),
                        null, null  // companyName and contactPerson don't apply to farmers
                );
            }
            case OFF_TAKER -> {
                OffTaker offTaker = offTakerRepository.findByUser(user)
                        .orElseThrow(() -> new ResourceNotFoundException("OffTaker profile not found"));

                yield new UserProfileResponse(
                        user.getId(), user.getEmail(), user.getRole().name(), user.getCreatedAt(),
                        null, null, offTaker.getPhone(), null, null, null,  // farmer fields don't apply
                        offTaker.getCompanyName(), offTaker.getContactPerson()
                );
            }
            case ADMIN -> new UserProfileResponse(
                    user.getId(), user.getEmail(), user.getRole().name(), user.getCreatedAt(),
                    null, null, null, null, null, null, null, null
            );
        };
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getRole().name(), user.getCreatedAt());
    }
}
