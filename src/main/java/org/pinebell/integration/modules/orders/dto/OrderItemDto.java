package org.pinebell.integration.modules.orders.dto;

import java.math.BigDecimal;

import org.pinebell.integration.modules.orders.domain.OrderItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record OrderItemDto(

    @NotBlank
    String name,

    @NotBlank
    @Size(min = 1, max = 15)
    String sku,

    @Positive
    @Positive
    Integer quantity,

    @NotNull
    @PositiveOrZero
    BigDecimal unitPrice

) {
}
