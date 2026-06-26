package org.pinebell.integration.modules.orders.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SyncOrderResponse(

    UUID externalId,

    String syncStatus,

    String message,

    LocalDateTime processedAt

) {
}
