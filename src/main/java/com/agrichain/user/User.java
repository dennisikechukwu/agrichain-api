package com.agrichain.user;

import com.agrichain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// @Entity tells Hibernate: "map this class to a database table"
// We name the table "users" because "user" is a reserved keyword in PostgreSQL
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    // unique = true creates a UNIQUE constraint in the DB — no two users can share an email
    @Column(nullable = false, unique = true)
    private String email;

    // This will store the BCrypt-hashed password, never plain text
    @Column(nullable = false)
    private String password;

    // @Enumerated(EnumType.STRING) stores the enum as a readable string in the DB
    // e.g. "FARMER" instead of 0, 1, 2 (which is what EnumType.ORDINAL would do)
    // Always use STRING — if you reorder your enum values, ORDINAL breaks your data
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ── UserDetails methods ────────────────────────────────────────────────────
    // Spring Security does not know about our Role enum. It works with GrantedAuthority
    // objects. This method translates our role into something Spring Security understands.
    // The "ROLE_" prefix is a Spring Security convention for role-based checks.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // Spring Security calls getUsername() to identify a user — we use email as the identifier
    @Override
    public String getUsername() {
        return email;
    }

    // These four methods are for account lifecycle management.
    // Returning true means: account is active, not locked, credentials are fresh, account is enabled.
    // You would set these to false in features like "suspend account" or "force password reset".
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
