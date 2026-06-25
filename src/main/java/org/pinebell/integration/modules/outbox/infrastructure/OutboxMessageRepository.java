package org.pinebell.integration.modules.outbox.infrastructure;

import org.pinebell.integration.modules.outbox.domain.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {
    
}
