package org.pinebell.integration.modules.orders.dto;

import java.math.BigDecimal;

import org.pinebell.integration.modules.orders.domain.OrderItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemDto(

    @NotBlank
    String name,

    @NotBlank
    @Size(min = 1, max = 15)
    String sku,

    @Positive
    Integer quantity,

    @NotNull
    BigDecimal unitPrice

) {
    public static ItemDto fromOrderItem(OrderItem orderItem) {
        return new ItemDto(
            orderItem.getName(),
            orderItem.getSku(),
            orderItem.getQuantity(),
            orderItem.getUnitPrice()
        );
    }
}
