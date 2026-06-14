package com.agrichain.farmer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record FarmerResponse(
        @Schema(example = "1") Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        String state,
        String lga,
        String farmSize,
        LocalDateTime createdAt
) {}
