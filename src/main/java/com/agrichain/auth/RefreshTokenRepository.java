package com.agrichain.auth;

import com.agrichain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Look up a refresh token by its string value — used when client sends it back
    Optional<RefreshToken> findByToken(String token);

    // On logout: mark ALL tokens for this user as revoked in a single SQL UPDATE
    // @Modifying tells Spring Data this query changes data (not just reads)
    // @Query lets us write JPQL (JPA's version of SQL) directly when the method name would be too complex
    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user")
    void revokeAllUserTokens(User user);
}
