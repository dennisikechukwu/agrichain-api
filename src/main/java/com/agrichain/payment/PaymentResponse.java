package com.agrichain.payment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long orderId,
        @Schema(example = "1") Long farmerId,
        String farmerName,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime paymentDate,
        LocalDateTime createdAt
) {}
