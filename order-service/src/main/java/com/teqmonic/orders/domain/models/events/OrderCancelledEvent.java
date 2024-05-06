package com.teqmonic.orders.domain.models.events;

import com.teqmonic.orders.domain.models.Address;
import com.teqmonic.orders.domain.models.Customer;
import com.teqmonic.orders.domain.models.OrderItem;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderCancelledEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        boolean isEventPublished,
        LocalDateTime createdAt) {}
