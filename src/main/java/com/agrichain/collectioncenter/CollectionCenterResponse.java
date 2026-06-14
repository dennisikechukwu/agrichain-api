package com.agrichain.collectioncenter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CollectionCenterResponse(
        @Schema(example = "1") Long id,
        String name,
        String state,
        String address,
        Integer capacity,
        String managerEmail,
        LocalDateTime createdAt
) {}
