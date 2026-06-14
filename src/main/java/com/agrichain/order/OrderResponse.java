package com.agrichain.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long offTakerId,
        String offTakerCompanyName,
        @Schema(example = "1") Long produceId,
        String produceName,
        String unit,
        @Schema(example = "1") Long collectionCenterId,
        String collectionCenterName,
        BigDecimal quantityRequested,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime orderDate,
        LocalDateTime createdAt
) {}
