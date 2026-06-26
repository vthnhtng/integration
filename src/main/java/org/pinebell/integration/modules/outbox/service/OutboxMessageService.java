package org.pinebell.integration.modules.outbox.service;

import org.pinebell.integration.modules.outbox.domain.OutboxMessage;
import org.pinebell.integration.modules.outbox.dto.CreateOutboxMessage;

public interface OutboxMessageService {

    OutboxMessage create(CreateOutboxMessage request);

}
