package org.pinebell.integration.modules.outbox.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox_messages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;
    private OutboxAggregateType aggregateType;
    private UUID aggregateUuid;
    private String messageType;
    private String payload;
    private LocalDateTime createdAt;
    private OutboxStatus status;
}
