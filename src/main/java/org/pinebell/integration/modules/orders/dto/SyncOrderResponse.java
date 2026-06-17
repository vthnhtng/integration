package org.pinebell.integration.modules.orders.dto;

import java.time.Instant;
import java.util.UUID;

public record SyncOrderResponse(

    UUID idempotencyKey,

    String requestId,

    String syncStatus,

    String message,

    Instant processedAt

) {
}