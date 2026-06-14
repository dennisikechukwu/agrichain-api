package com.agrichain.auth;

import com.agrichain.common.exception.ResourceNotFoundException;
import com.agrichain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Reads refresh-token-expiration from application.yaml
    // The value is in milliseconds (604800000 = 7 days)
    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    // Creates a new refresh token for a user and saves it to the database.
    // Called after a successful register or login.
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())  // random, unique, unguessable
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // Looks up a refresh token string in the database.
    // Returns Optional — the token might not exist if it was never issued or was deleted.
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // Validates that a refresh token is still usable:
    //   1. Not expired (expiryDate is still in the future)
    //   2. Not revoked (wasn't used already or logged out)
    // If either check fails, we revoke it in the DB and throw an exception.
    // The caller (AuthService) catches this and returns a 401 to the client.
    public RefreshToken verifyRefreshToken(RefreshToken token) {
        if (token.isRevoked()) {
            throw new ResourceNotFoundException("Refresh token has already been used or revoked. Please log in again.");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new ResourceNotFoundException("Refresh token has expired. Please log in again.");
        }

        return token;
    }

    // Token rotation — called when a client uses their refresh token to get a new access token.
    // We revoke the old token and issue a brand new one.
    // This means each refresh token can only be used ONCE, then it's replaced.
    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        // Mark old token as revoked — it can never be used again
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        // Issue a fresh token for the same user
        return createRefreshToken(oldToken.getUser());
    }

    // Called on logout — invalidates all active sessions for this user across all devices.
    // After this, no device can refresh their access token until they log in again.
    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllUserTokens(user);
    }
}
