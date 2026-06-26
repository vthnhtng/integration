package org.pinebell.integration.modules.orders.domain;

import org.pinebell.integration.modules.orders.mapper.OrderMapper;
import org.pinebell.integration.modules.outbox.domain.OutboxAggregateType;
import org.pinebell.integration.modules.outbox.domain.OutboxMessageAction;
import org.pinebell.integration.modules.outbox.dto.CreateOutboxMessage;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tools.jackson.databind.ObjectMapper;

@Component
public class OrderOutboxMessageFactory {

    private final ObjectMapper objectMapper;
    private final OrderMapper orderMapper;

    public OrderOutboxMessageFactory(ObjectMapper objectMapper, OrderMapper orderMapper) {
        this.objectMapper = objectMapper;
        this.orderMapper = orderMapper;
    }

    @SneakyThrows
    public CreateOutboxMessage create(Order order, OutboxMessageAction action) {
        if (action != OutboxMessageAction.SYNC) {
            throw new IllegalArgumentException("Unsupported outbox action for order: " + action);
        }

        String payload = objectMapper.writeValueAsString(orderMapper.toDto(order));

        return new CreateOutboxMessage(
            OutboxAggregateType.ORDER,
            order.getUuid(),
            OutboxAggregateType.ORDER.name() + "_" + action.name(),
            payload
        );
    }
}
