package com.teqmonic.notifications.domain.models.events;

import com.teqmonic.notifications.domain.models.Address;
import com.teqmonic.notifications.domain.models.Customer;
import com.teqmonic.notifications.domain.models.OrderItem;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        boolean isEventPublished,
        LocalDateTime createdAt) {}
