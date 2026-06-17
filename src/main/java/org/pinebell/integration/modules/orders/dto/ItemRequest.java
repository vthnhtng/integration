package org.pinebell.integration.modules.orders.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemRequest(

    @NotBlank
    String sku,

    @NotBlank
    String name,

    @Positive
    Integer quantity,

    @NotNull
    BigDecimal unitPrice
) {
}