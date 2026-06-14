package com.agrichain.produce;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProduceRequest(

        @NotBlank(message = "Produce name is required")
        String name,

        @NotBlank(message = "Category is required")
        String category,

        @NotBlank(message = "Unit is required (e.g. KG, TONNES, BAGS)")
        String unit,

        @NotNull(message = "Price per unit is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal pricePerUnit
) {}
