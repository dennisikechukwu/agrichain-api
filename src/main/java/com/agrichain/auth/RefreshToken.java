package com.agrichain.auth;

import com.agrichain.common.BaseEntity;
import com.agrichain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// A refresh token is stored in the database — this is what separates it from the access token.
// The access token is a self-contained JWT (no DB lookup needed to validate it).
// The refresh token is an opaque string we store so we can:
//   1. Check if it has been used before (detect theft)
//   2. Revoke it on logout
//   3. Invalidate all sessions for a user if their account is compromised

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {

    // The actual token value — a UUID string (e.g. "550e8400-e29b-41d4-a716-446655440000")
    // UUID is 128 bits of randomness — impossible to guess
    // unique = true means no two tokens can have the same value
    @Column(nullable = false, unique = true)
    private String token;

    // One User can have multiple active refresh tokens (one per device/browser session)
    // e.g. logged in on phone AND laptop at the same time
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    // When this token expires — set to now + 7 days when created
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // When a refresh token is used, we mark it revoked and issue a NEW one.
    // This is called "token rotation" — if a stolen token is ever used,
    // the real user's next request will fail (their token was rotated away),
    // which signals a potential security breach.
    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;
}
