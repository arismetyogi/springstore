package com.arism.dto;

import jakarta.validation.constraints.Positive;

public class CartItemDto {
    private Long id;
    private Long productId;
    @Positive(message = "Cannot be negative")
    private Integer quantity;
}
