package com.agrichain.farmerproduce;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record FarmerProduceRequest(
        @NotNull(message = "Farmer ID is required")
        @Schema(example = "1")
        Long farmerId,

        @NotNull(message = "Produce ID is required")
        @Schema(example = "1")
        Long produceId,

        @NotNull(message = "Quantity available is required")
        @Positive(message = "Quantity must be greater than zero")
        @Schema(example = "250")
        BigDecimal quantityAvailable,

        @NotBlank(message = "Season is required (e.g. 2025 Wet Season)")
        String season
) {}
