package com.teqmonic.orders.domain;

import com.teqmonic.orders.domain.models.CreateOrderRequest;
import com.teqmonic.orders.domain.models.OrderItem;
import com.teqmonic.orders.domain.models.OrderStatusEnum;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OrderMapper {

    static CustomerEntity buildCustomerEntity(CreateOrderRequest request) {
        return CustomerEntity.builder()
                .customer(request.customer())
                .address(request.deliveryAddress())
                .build();
    }

    static OrderEntity convertToEntity(CreateOrderRequest request, CustomerEntity customerEntity) {
        OrderEntity newOrder = OrderEntity.builder()
                .orderNumber(UUID.randomUUID().toString())
                .status(OrderStatusEnum.NEW)
                .customer(customerEntity)
                .build();
        // Order items
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for (OrderItem item : request.items()) {
            orderItems.add(OrderItemEntity.builder()
                    .code(item.code())
                    .name(item.name())
                    .price(item.price())
                    .quantity(item.quantity())
                    .order(newOrder)
                    .build());
        }
        newOrder.setOrderItem(orderItems);
        return newOrder;
    }
}
