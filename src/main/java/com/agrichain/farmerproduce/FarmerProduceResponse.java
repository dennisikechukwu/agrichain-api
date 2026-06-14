package com.agrichain.farmerproduce;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FarmerProduceResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long farmerId,
        String farmerName,
        @Schema(example = "1") Long produceId,
        String produceName,
        String unit,
        BigDecimal quantityAvailable,
        String season,
        LocalDateTime createdAt
) {}
