package com.agrichain.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserProfileResponse(
        @Schema(example = "1") Long id,
        String email,
        String role,
        LocalDateTime createdAt,

        // FARMER fields
        String firstName,
        String lastName,
        String phone,
        String state,
        String lga,
        String farmSize,

        // OFF_TAKER fields
        String companyName,
        String contactPerson
) {}
