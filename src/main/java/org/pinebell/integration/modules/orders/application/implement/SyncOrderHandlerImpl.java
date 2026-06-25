package org.pinebell.integration.modules.orders.application.implement;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.pinebell.integration.modules.orders.application.SyncOrderHandler;
import org.pinebell.integration.modules.orders.domain.Order;
import org.pinebell.integration.modules.orders.domain.OrderItem;
import org.pinebell.integration.modules.orders.dto.OrderDto;
import org.pinebell.integration.modules.orders.dto.SyncOrderRequest;
import org.pinebell.integration.modules.orders.error.OrderExistsException;
import org.pinebell.integration.modules.orders.infrastructure.OrderRepository;
import org.pinebell.integration.modules.outbox.domain.OutboxAggregateType;
import org.pinebell.integration.modules.outbox.domain.OutboxMessage;
import org.pinebell.integration.modules.outbox.dto.CreateOutboxMessageInput;
import org.pinebell.integration.modules.outbox.service.CreateOutboxMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

@Service
public class SyncOrderHandlerImpl implements SyncOrderHandler {

    private OrderRepository orderRepository;

    private CreateOutboxMessage createOutboxMessage;

    private ObjectMapper jsonSerializer;

    public SyncOrderHandlerImpl(
        OrderRepository orderRepository,
        CreateOutboxMessage createOutboxMessage,
        ObjectMapper jsonSerializer
    ) {
        this.orderRepository = orderRepository;
        this.createOutboxMessage = createOutboxMessage;
        this.jsonSerializer = jsonSerializer;
    }

    @Override
    @Transactional
    public Order execute(SyncOrderRequest request) {
        Order order = createOrder(request);

        createOutboxMessage(order);

        return order;
    }

    private Order createOrder(SyncOrderRequest request) {
        OrderDto orderRequest = request.order();

        String orderSource = orderRequest.source();
        String orderNumber = orderRequest.orderNumber();

        if (orderRepository.existsBySourceAndNumber(orderSource, orderNumber)) {
            throw new OrderExistsException(
                String.format("Order from %s with number %s is existed", orderSource, orderNumber)
            );
        }

        Order order = Order.builder()
            .uuid(UUID.randomUUID())
            .source(orderSource)
            .number(orderNumber)
            .currency(orderRequest.currency())
            .totalAmount(orderRequest.totalAmount())
            .build();

        List<OrderItem> items = prepareOrderItems(orderRequest);
        order.addItems(items);

        return orderRepository.save(order);
    }

    private List<OrderItem> prepareOrderItems(OrderDto orderRequest) {
        return orderRequest.items()
            .stream()
            .map(itemRequest -> OrderItem.builder()
                .name(itemRequest.name())
                .sku(itemRequest.sku())
                .quantity(itemRequest.quantity())
                .unitPrice(itemRequest.unitPrice())
                .build())
            .collect(Collectors.toList());
    }

    private OutboxMessage createOutboxMessage(Order order) {
        CreateOutboxMessageInput input = createMessageInput(order);

        return createOutboxMessage.execute(input);
    }

    private CreateOutboxMessageInput createMessageInput(Order order) {
        String payload = jsonSerializer.writeValueAsString(
            OrderDto.fromOrder(order)
        );

        return CreateOutboxMessageInput.builder()
            .aggregateType(OutboxAggregateType.ORDER)
            .aggregateUUID(order.getUuid())
            .messageType("ORDER_CREATE")
            .payload(payload)
            .build();
    }
}
