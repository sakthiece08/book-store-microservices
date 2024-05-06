package com.teqmonic.orders.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teqmonic.orders.domain.models.OrderEventType;
import com.teqmonic.orders.domain.models.events.OrderCancelledEvent;
import com.teqmonic.orders.domain.models.events.OrderCreatedEvent;
import com.teqmonic.orders.domain.models.events.OrderDeliveredEvent;
import com.teqmonic.orders.domain.models.events.OrderFailedEvent;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class OrderEventService {

    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEventEntity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .orderNumber(event.orderNumber())
                .eventType(OrderEventType.ORDER_CREATED)
                .createdAt(event.createdAt())
                .payload(toJsonPayload(event))
                .isEventPublished(event.isEventPublished())
                .build();
        orderEventRepository.save(orderEventEntity);
    }

    void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEventEntity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .orderNumber(event.orderNumber())
                .eventType(OrderEventType.ORDER_DELIVERED)
                .createdAt(event.createdAt())
                .payload(toJsonPayload(event))
                .isEventPublished(event.isEventPublished())
                .build();
        orderEventRepository.save(orderEventEntity);
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEventEntity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .orderNumber(event.orderNumber())
                .eventType(OrderEventType.ORDER_CANCELLED)
                .createdAt(event.createdAt())
                .payload(toJsonPayload(event))
                .isEventPublished(event.isEventPublished())
                .build();
        orderEventRepository.save(orderEventEntity);
    }

    void save(OrderFailedEvent event) {
        OrderEventEntity orderEventEntity = OrderEventEntity.builder()
                .eventId(event.eventId())
                .orderNumber(event.orderNumber())
                .eventType(OrderEventType.ORDER_CANCELLED)
                .createdAt(event.createdAt())
                .payload(toJsonPayload(event))
                .isEventPublished(event.isEventPublished())
                .build();
        orderEventRepository.save(orderEventEntity);
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        // use pageable in case of more records
        List<OrderEventEntity> events = orderEventRepository.findAllByIsEventPublished(sort, false);
        log.info("Found {} Order Events to be published", events.size());
        for (OrderEventEntity event : events) {
            log.info("Publishing Event Id: {}, Order number: {}", event.getEventId(), event.getOrderNumber());
            this.publishEvent(event);
            event.setEventPublished(true);
            event.setUpdatedAt(LocalDateTime.now());
            orderEventRepository.save(event);
            log.info(
                    "Updated Event published to true for Event Id: {}, Order number: {}",
                    event.getEventId(),
                    event.getOrderNumber());
        }
    }

    private void publishEvent(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            case ORDER_DELIVERED:
                OrderDeliveredEvent orderDeliveredEvent =
                        fromJsonPayload(event.getPayload(), OrderDeliveredEvent.class);
                orderEventPublisher.publish(orderDeliveredEvent);
                break;
            case ORDER_CANCELLED:
                OrderCancelledEvent orderCancelledEvent =
                        fromJsonPayload(event.getPayload(), OrderCancelledEvent.class);
                orderEventPublisher.publish(orderCancelledEvent);
                break;
            default:
                log.warn("Unsupported Order Event Type: {}", eventType);
        }
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
