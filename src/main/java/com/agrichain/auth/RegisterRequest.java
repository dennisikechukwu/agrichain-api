package com.agrichain.auth;

import com.agrichain.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotNull(message = "Role is required")
        Role role,

        // Farmer-specific fields
        String firstName,
        String lastName,
        String phone,
        String state,
        String lga,
        String farmSize,

        // OffTaker-specific fields
        String companyName,
        String contactPerson
) {}
