package org.pinebell.integration.modules.outbox.service;

import org.pinebell.integration.modules.outbox.domain.OutboxMessage;
import org.pinebell.integration.modules.outbox.dto.CreateOutboxMessageInput;

public interface CreateOutboxMessage {

    public OutboxMessage execute(CreateOutboxMessageInput input);

}
