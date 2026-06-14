package com.agrichain.payment;

import jakarta.validation.constraints.NotNull;

public record UpdatePaymentStatusRequest(
        @NotNull(message = "Status is required")
        PaymentStatus status
) {}
