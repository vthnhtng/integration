package org.pinebell.integration.modules.orders.domain;

import java.math.BigDecimal;

import org.pinebell.integration.modules.orders.dto.OrderItemDto;
import org.pinebell.integration.modules.orders.error.order.InvalidOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderItemFactory {

    private static final int SKU_MAX_LENGTH = 15;

    public OrderItem create(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            throw new InvalidOrderException("OrderItemDto must not be null");
        }

        validateName(orderItemDto);
        validateSku(orderItemDto);
        validateQuantity(orderItemDto);
        validateUnitPrice(orderItemDto);

        return OrderItem.builder()
            .name(orderItemDto.name())
            .sku(orderItemDto.sku())
            .quantity(orderItemDto.quantity())
            .unitPrice(orderItemDto.unitPrice())
            .build();
    }

    private void validateName(OrderItemDto dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new InvalidOrderException(
                String.format("Item with SKU '%s' has a blank or null name", dto.sku())
            );
        }
    }

    private void validateSku(OrderItemDto dto) {
        if (dto.sku() == null || dto.sku().isBlank()) {
            throw new InvalidOrderException("Item SKU must not be blank or null");
        }
        if (dto.sku().length() > SKU_MAX_LENGTH) {
            throw new InvalidOrderException(
                String.format(
                    "Item SKU '%s' exceeds the maximum allowed length of %d characters",
                    dto.sku(), SKU_MAX_LENGTH
                )
            );
        }
    }

    private void validateQuantity(OrderItemDto dto) {
        if (dto.quantity() == null) {
            throw new InvalidOrderException(
                String.format("Item with SKU '%s' has a null quantity", dto.sku())
            );
        }
        if (dto.quantity() <= 0) {
            throw new InvalidOrderException(
                String.format("Item with SKU '%s' has an invalid quantity %d — must be greater than zero", dto.sku(), dto.quantity())
            );
        }
    }

    private void validateUnitPrice(OrderItemDto dto) {
        if (dto.unitPrice() == null) {
            throw new InvalidOrderException(
                String.format("Item with SKU '%s' has a null unit price", dto.sku())
            );
        }
        if (dto.unitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderException(
                String.format("Item with SKU '%s' has a negative unit price %s", dto.sku(), dto.unitPrice())
            );
        }
    }
}
