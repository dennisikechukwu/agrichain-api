package com.agrichain.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull(message = "Off-taker ID is required")
        @Schema(example = "1")
        Long offTakerId,

        @NotNull(message = "Produce ID is required")
        @Schema(example = "1")
        Long produceId,

        @NotNull(message = "Collection center ID is required")
        @Schema(example = "1")
        Long collectionCenterId,

        @NotNull(message = "Quantity requested is required")
        @Positive(message = "Quantity must be a positive number")
        @Schema(example = "500")
        BigDecimal quantityRequested
) {}
