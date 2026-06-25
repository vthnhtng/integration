package org.pinebell.integration.modules.orders.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.pinebell.integration.modules.orders.domain.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderDto(

    @NotBlank
    String source,

    @NotBlank
    String orderNumber,

    @NotBlank
    String currency,

    @NotNull
    BigDecimal totalAmount,

    @Valid
    @NotEmpty
    List<ItemDto> items
) {
    public static OrderDto fromOrder(Order order) {
        return new OrderDto(
            order.getSource(),
            order.getNumber(),
            order.getCurrency(),
            order.getTotalAmount(),
            order.getItems()
                .stream()
                .map(item -> ItemDto.fromOrderItem(item))
                .collect(Collectors.toList())
        );
    }
}
