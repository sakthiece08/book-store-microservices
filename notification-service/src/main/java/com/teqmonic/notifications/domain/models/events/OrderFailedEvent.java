package com.teqmonic.notifications.domain.models.events;

import com.teqmonic.notifications.domain.models.Address;
import com.teqmonic.notifications.domain.models.Customer;
import com.teqmonic.notifications.domain.models.OrderItem;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderFailedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        boolean isEventPublished,
        LocalDateTime createdAt) {}
