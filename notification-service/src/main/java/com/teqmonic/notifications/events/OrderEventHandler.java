package com.teqmonic.notifications.events;

import com.teqmonic.notifications.domain.NotificationService;
import com.teqmonic.notifications.domain.OrderEventEntity;
import com.teqmonic.notifications.domain.OrderEventRepository;
import com.teqmonic.notifications.domain.models.events.OrderCancelledEvent;
import com.teqmonic.notifications.domain.models.events.OrderCreatedEvent;
import com.teqmonic.notifications.domain.models.events.OrderDeliveredEvent;
import com.teqmonic.notifications.domain.models.events.OrderFailedEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class OrderEventHandler {

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCreatedEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity entity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .createdAt(LocalDateTime.now())
                .build();
        orderEventRepository.save(entity);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Received OrderDeliveredEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderDeliveredEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity entity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .createdAt(LocalDateTime.now())
                .build();
        orderEventRepository.save(entity);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Received OrderCancelledEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCancelledEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        OrderEventEntity entity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .createdAt(LocalDateTime.now())
                .build();
        orderEventRepository.save(entity);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderFailedEvent(OrderFailedEvent event) {
        log.info("Received OrderFailedEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderFailedEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity entity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .createdAt(LocalDateTime.now())
                .build();
        orderEventRepository.save(entity);
    }
}
