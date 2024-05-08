package com.teqmonic.notifications.events;

import com.teqmonic.notifications.domain.NotificationService;
import com.teqmonic.notifications.domain.models.events.OrderCancelledEvent;
import com.teqmonic.notifications.domain.models.events.OrderCreatedEvent;
import com.teqmonic.notifications.domain.models.events.OrderDeliveredEvent;
import com.teqmonic.notifications.domain.models.events.OrderFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class OrderEventHandler {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: {}", event);
        notificationService.sendOrderCreatedNotification(event);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Received OrderDeliveredEvent: {}", event);
        notificationService.sendOrderDeliveredNotification(event);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Received OrderCancelledEvent: {}", event);
        notificationService.sendOrderCancelledNotification(event);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderFailedEvent(OrderFailedEvent event) {
        log.info("Received OrderFailedEvent: {}", event);
        notificationService.sendOrderErrorEventNotification(event);
    }
}
