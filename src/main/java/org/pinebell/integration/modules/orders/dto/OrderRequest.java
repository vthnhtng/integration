package org.pinebell.integration.modules.orders.dto;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(

    @NotBlank
    String orderNumber,

    @Valid
    @NotEmpty
    Set<ItemRequest> items,

    @NotBlank
    String currency,

    @NotNull
    BigDecimal totalAmount
) {
}