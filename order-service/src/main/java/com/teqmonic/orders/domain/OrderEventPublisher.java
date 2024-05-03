package com.teqmonic.orders.domain;

import com.teqmonic.orders.ApplicationProperties;
import com.teqmonic.orders.domain.models.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;
    private final Binding bindingNewOrderQueue;

    public void publish(OrderCreatedEvent event) {
        this.send(event);
    }

    private void send(Object payload) {
        log.info("bindingNewOrderQueue getRoutingKey:: {}", bindingNewOrderQueue.getRoutingKey());
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), "new-order-queue-routing-key", payload);
    }
}
