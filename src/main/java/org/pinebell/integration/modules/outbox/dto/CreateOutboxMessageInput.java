package org.pinebell.integration.modules.outbox.dto;

import java.util.UUID;

import org.pinebell.integration.modules.outbox.domain.OutboxAggregateType;

import lombok.Builder;

@Builder
public record CreateOutboxMessageInput(
    OutboxAggregateType aggregateType,
    UUID aggregateUUID,
    String messageType,
    String payload
) {    
}
