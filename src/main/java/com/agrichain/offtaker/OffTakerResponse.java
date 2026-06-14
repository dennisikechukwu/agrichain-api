package com.agrichain.offtaker;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record OffTakerResponse(
        @Schema(example = "1") Long id,
        String email,
        String companyName,
        String contactPerson,
        String phone,
        LocalDateTime createdAt
) {}
