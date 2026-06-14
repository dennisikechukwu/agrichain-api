package com.agrichain.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OffTakerRegisterRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Company name is required")
        String companyName,

        @NotBlank(message = "Contact person is required")
        String contactPerson,

        @NotBlank(message = "Phone is required")
        String phone
) {}
