package org.pinebell.integration.modules.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pinebell.integration.modules.orders.domain.Order;
import org.pinebell.integration.modules.orders.domain.OrderItem;
import org.pinebell.integration.modules.orders.dto.OrderDto;
import org.pinebell.integration.modules.orders.dto.OrderItemDto;
import org.pinebell.integration.modules.orders.dto.SyncOrderResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderNumber", source = "number")
    OrderDto toDto(Order order);

    OrderItemDto toDto(OrderItem orderItem);
}
