package org.pinebell.integration.modules.outbox.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.pinebell.integration.modules.outbox.domain.OutboxMessage;
import org.pinebell.integration.modules.outbox.domain.OutboxStatus;
import org.pinebell.integration.modules.outbox.dto.CreateOutboxMessageInput;
import org.pinebell.integration.modules.outbox.infrastructure.OutboxMessageRepository;
import org.pinebell.integration.modules.outbox.service.CreateOutboxMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOutboxMessageImpl implements CreateOutboxMessage {

    private OutboxMessageRepository outboxMessageRepository;

    public CreateOutboxMessageImpl(
        OutboxMessageRepository outboxMessageRepository
    ) {
        this.outboxMessageRepository = outboxMessageRepository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public OutboxMessage execute(CreateOutboxMessageInput input) {
        OutboxMessage message = OutboxMessage.builder()
            .uuid(UUID.randomUUID())
            .aggregateType(input.aggregateType())
            .aggregateUuid(input.aggregateUUID())
            .messageType(input.messageType())
            .payload(input.payload())
            .createdAt(LocalDateTime.now())
            .status(OutboxStatus.PENDING)
            .build();

        return outboxMessageRepository.save(message);
    }
}
