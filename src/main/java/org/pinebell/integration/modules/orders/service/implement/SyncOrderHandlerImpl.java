package org.pinebell.integration.modules.orders.service.implement;

import org.pinebell.integration.modules.orders.domain.Order;
import org.pinebell.integration.modules.orders.domain.OrderFactory;
import org.pinebell.integration.modules.orders.domain.OrderOutboxMessageFactory;
import org.pinebell.integration.modules.orders.dto.OrderDto;
import org.pinebell.integration.modules.orders.dto.SyncOrderRequest;
import org.pinebell.integration.modules.orders.dto.SyncOrderResponse;
import org.pinebell.integration.modules.orders.error.OrderExistsException;
import org.pinebell.integration.modules.orders.infrastructure.OrderRepository;
import org.pinebell.integration.modules.orders.service.SyncOrderHandler;
import org.pinebell.integration.modules.outbox.domain.OutboxMessage;
import org.pinebell.integration.modules.outbox.domain.OutboxMessageAction;
import org.pinebell.integration.modules.outbox.domain.OutboxStatus;
import org.pinebell.integration.modules.outbox.dto.CreateOutboxMessage;
import org.pinebell.integration.modules.outbox.service.OutboxMessageService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SyncOrderHandlerImpl implements SyncOrderHandler {

    private final OrderFactory orderFactory;
    private final OrderRepository orderRepository;
    private final OutboxMessageService outboxMessageService;
    private final OrderOutboxMessageFactory orderOutboxMessageFactory;

    public SyncOrderHandlerImpl(
        OrderFactory orderFactory,
        OrderRepository orderRepository,
        OutboxMessageService outboxMessageService,
        OrderOutboxMessageFactory orderOutboxMessageFactory
    ) {
        this.orderFactory = orderFactory;
        this.orderRepository = orderRepository;
        this.outboxMessageService = outboxMessageService;
        this.orderOutboxMessageFactory = orderOutboxMessageFactory;
    }

    @Override
    @Transactional
    public SyncOrderResponse execute(SyncOrderRequest request) {
        Order order = createOrder(request);

        createOutboxMessage(order);

        return new SyncOrderResponse(
            order.getUuid(),
            OutboxStatus.PENDING.toString(),
            String.format("Your order number %s from %s is recorded", order.getNumber(), order.getSource()),
            java.time.LocalDateTime.now()
        );
    }

    private Order createOrder(SyncOrderRequest request) {
        OrderDto orderDto = request.order();

        Order order = orderFactory.create(orderDto);

        try {
            return orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new OrderExistsException(
                String.format(
                    "Order from %s with number %s is existed",
                    order.getSource(),
                    order.getNumber()
                )
            );
        }
    }

    private OutboxMessage createOutboxMessage(Order order) {
        CreateOutboxMessage outboxRequest = orderOutboxMessageFactory.create(order, OutboxMessageAction.SYNC);
        return outboxMessageService.create(outboxRequest);
    }
}
