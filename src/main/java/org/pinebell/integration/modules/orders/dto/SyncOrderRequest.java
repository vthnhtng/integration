package org.pinebell.integration.modules.orders.dto;

import java.time.Instant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SyncOrderRequest(

    @NotBlank
    String eventType,

    @NotNull
    Instant eventTimestamp,

    @Valid
    @NotNull
    OrderDto order

) {
}