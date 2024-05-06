package com.teqmonic.orders.domain;

import com.teqmonic.orders.domain.models.OrderItem;
import com.teqmonic.orders.domain.models.events.OrderCancelledEvent;
import com.teqmonic.orders.domain.models.events.OrderCreatedEvent;
import com.teqmonic.orders.domain.models.events.OrderDeliveredEvent;
import com.teqmonic.orders.domain.models.events.OrderFailedEvent;
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
                false,
                LocalDateTime.now());
    }

    static OrderDeliveredEvent buildOrderDeliveredEvent(OrderEntity order) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer().getCustomer(),
                order.getCustomer().getAddress(),
                false,
                LocalDateTime.now());
    }

    static OrderCancelledEvent buildOrderCancelledEvent(OrderEntity order, String reason) {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer().getCustomer(),
                order.getCustomer().getAddress(),
                reason,
                false,
                LocalDateTime.now());
    }

    static OrderFailedEvent buildOrderFailedEvent(OrderEntity order, String reason) {
        return new OrderFailedEvent(
                UUID.randomUUID().toString(),
                order.getOrderNumber(),
                getOrderItems(order),
                order.getCustomer().getCustomer(),
                order.getCustomer().getAddress(),
                reason,
                false,
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
