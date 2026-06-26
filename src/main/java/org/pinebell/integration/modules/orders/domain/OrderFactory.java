package org.pinebell.integration.modules.orders.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.pinebell.integration.modules.orders.dto.OrderDto;
import org.pinebell.integration.modules.orders.error.order.InvalidOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {

    private static final int CURRENCY_CODE_LENGTH = 3;

    private final OrderItemFactory orderItemFactory;

    public OrderFactory(OrderItemFactory orderItemFactory) {
        this.orderItemFactory = orderItemFactory;
    }

    public Order create(OrderDto orderDto) {
        if (orderDto == null) {
            throw new InvalidOrderException("OrderDto must not be null");
        }

        validateSource(orderDto);
        validateOrderNumber(orderDto);
        validateCurrency(orderDto);
        validateTotalAmount(orderDto);
        validateItems(orderDto);

        List<OrderItem> items = orderDto.items()
            .stream()
            .map(orderItemFactory::create)
            .collect(Collectors.toList());

        Order order = Order.builder()
            .uuid(UUID.randomUUID())
            .source(orderDto.source())
            .number(orderDto.orderNumber())
            .currency(orderDto.currency().toUpperCase())
            .totalAmount(orderDto.totalAmount())
            .build();

        order.addItems(items);

        return order;
    }

    private void validateSource(OrderDto dto) {
        if (dto.source() == null || dto.source().isBlank()) {
            throw new InvalidOrderException("Order source must not be blank or null");
        }
    }

    private void validateOrderNumber(OrderDto dto) {
        if (dto.orderNumber() == null || dto.orderNumber().isBlank()) {
            throw new InvalidOrderException("Order number must not be blank or null");
        }
    }

    private void validateCurrency(OrderDto dto) {
        if (dto.currency() == null || dto.currency().isBlank()) {
            throw new InvalidOrderException("Order currency must not be blank or null");
        }

        if (dto.currency().trim().length() != CURRENCY_CODE_LENGTH) {
            throw new InvalidOrderException(
                String.format(
                    "Order currency '%s' is invalid — must be a 3-letter ISO 4217 code",
                    dto.currency()
                )
            );
        }
    }

    private void validateTotalAmount(OrderDto dto) {
        if (dto.totalAmount() == null) {
            throw new InvalidOrderException("Order total amount must not be null");
        }
        if (dto.totalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderException(
                String.format(
                    "Order total amount %s is invalid — must be zero or greater",
                    dto.totalAmount()
                )
            );
        }
    }

    private void validateItems(OrderDto dto) {
        if (dto.items() == null || dto.items().isEmpty()) {
            throw new InvalidOrderException(
                String.format(
                    "Order '%s' from source '%s' must contain at least one item",
                    dto.orderNumber(), dto.source()
                )
            );
        }
    }
}
