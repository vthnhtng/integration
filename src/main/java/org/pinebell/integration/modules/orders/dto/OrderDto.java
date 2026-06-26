package org.pinebell.integration.modules.orders.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.pinebell.integration.modules.orders.domain.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderDto(

    @NotBlank
    String source,

    @NotBlank
    String orderNumber,

    @NotBlank
    String currency,

    @NotNull
    @PositiveOrZero
    BigDecimal totalAmount,

    @Valid
    @NotEmpty
    List<OrderItemDto> items
) {
}
