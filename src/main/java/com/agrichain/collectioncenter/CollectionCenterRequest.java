package com.agrichain.collectioncenter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CollectionCenterRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be a positive number")
        Integer capacity
) {}
