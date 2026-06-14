package com.agrichain.produce;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProduceResponse(
        @Schema(example = "1") Long id,
        String name,
        String category,
        String unit,
        BigDecimal pricePerUnit,
        LocalDateTime createdAt
) {}
