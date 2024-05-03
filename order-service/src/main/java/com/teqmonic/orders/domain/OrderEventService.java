package com.teqmonic.orders.domain;

import static org.hibernate.query.sqm.tree.SqmNode.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teqmonic.orders.domain.models.OrderCreatedEvent;
import com.teqmonic.orders.domain.models.OrderEventType;
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
                .build();
        orderEventRepository.save(orderEventEntity);
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        // use pageable in case of more records
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        log.info("Found {} Order Events to be published", events.size());
        for (OrderEventEntity event : events) {
            log.info("Processing Event Id: {}, Order number: {}", event.getEventId(), event.getOrderNumber());
            this.publishEvent(event);
            orderEventRepository.delete(event);
        }
    }

    private void publishEvent(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
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
