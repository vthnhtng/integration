package org.pinebell.integration.modules.outbox.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.pinebell.integration.modules.outbox.domain.OutboxAggregateType;
import org.pinebell.integration.modules.outbox.domain.OutboxStatus;

import lombok.Builder;

@Builder
public record OutboxMessageDto(
    UUID uuid,
    OutboxAggregateType aggregateType,
    UUID aggregateUuid,
    String messageType,
    String payload,
    LocalDateTime createdAt,
    OutboxStatus status
) {
}
