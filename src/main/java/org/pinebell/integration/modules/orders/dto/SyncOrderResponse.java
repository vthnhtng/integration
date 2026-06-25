package org.pinebell.integration.modules.orders.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.pinebell.integration.modules.orders.domain.Order;
import org.pinebell.integration.modules.outbox.domain.OutboxStatus;

public record SyncOrderResponse(

    UUID externalId,

    String syncStatus,

    String message,

    LocalDateTime processedAt

) {
    public static SyncOrderResponse fromOrder(Order order) {
        String message = String.format(
            "Your order number %s from %s is recorded",
            order.getNumber(),
            order.getSource()
        );

        return new SyncOrderResponse(
            order.getUuid(),
            OutboxStatus.PENDING.toString(),
            message,
            LocalDateTime.now()
        );
    }
}
