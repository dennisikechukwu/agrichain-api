package com.agrichain.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull(message = "Order ID is required")
        @Schema(example = "1")
        Long orderId,

        @NotNull(message = "Farmer ID is required")
        @Schema(example = "1")
        Long farmerId
) {}
