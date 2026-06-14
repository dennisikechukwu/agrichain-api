package com.agrichain.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserResponse(
        @Schema(example = "1") Long id,
        String email,
        String role,
        LocalDateTime createdAt
) {}
