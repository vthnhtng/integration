package org.pinebell.integration.modules.outbox.dto;

import java.util.UUID;
import org.pinebell.integration.modules.outbox.domain.OutboxAggregateType;

public record CreateOutboxMessage(
    OutboxAggregateType aggregateType,
    UUID aggregateUuid,
    String messageType,
    String payload
) {
}
