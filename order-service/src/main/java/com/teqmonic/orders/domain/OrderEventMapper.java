package com.teqmonic.orders.domain;

import com.teqmonic.orders.domain.models.OrderCreatedEvent;
import com.teqmonic.orders.domain.models.OrderItem;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEventMapper {

    static OrderCreatedEvent buildOrderCreatedEvent(OrderEntity order) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer().getCustomer(),
                order.getCustomer().getAddress(),
                LocalDateTime.now());
    }

    private static Set<OrderItem> getOrderItems(OrderEntity order) {
        return order.getOrderItem().stream()
                .map(orderItemEntity -> new OrderItem(
                        orderItemEntity.getCode(),
                        orderItemEntity.getName(),
                        orderItemEntity.getPrice(),
                        orderItemEntity.getQuantity()))
                .collect(Collectors.toSet());
    }
}
