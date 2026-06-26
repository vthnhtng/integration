package org.pinebell.integration.modules.outbox.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.pinebell.integration.modules.outbox.domain.OutboxMessage;
import org.pinebell.integration.modules.outbox.domain.OutboxStatus;
import org.pinebell.integration.modules.outbox.dto.CreateOutboxMessage;
import org.pinebell.integration.modules.outbox.infrastructure.OutboxMessageRepository;
import org.pinebell.integration.modules.outbox.service.OutboxMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxMessageServiceImpl implements OutboxMessageService {

    private final OutboxMessageRepository outboxMessageRepository;

    public OutboxMessageServiceImpl(OutboxMessageRepository outboxMessageRepository) {
        this.outboxMessageRepository = outboxMessageRepository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public OutboxMessage create(CreateOutboxMessage request) {
        OutboxMessage outboxMessage = OutboxMessage.builder()
            .uuid(UUID.randomUUID())
            .aggregateType(request.aggregateType())
            .aggregateUuid(request.aggregateUuid())
            .messageType(request.messageType())
            .payload(request.payload())
            .createdAt(LocalDateTime.now())
            .status(OutboxStatus.PENDING)
            .build();

        return outboxMessageRepository.save(outboxMessage);
    }
}
