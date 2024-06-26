package com.teqmonic.notifications.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teqmonic.notifications.ApplicationProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMQConfig {

    private final ApplicationProperties properties;

    public RabbitMQConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(properties.orderEventsExchange());
    }

    @Bean
    Queue newOrderQueue() {
        return QueueBuilder.durable(properties.newOrdersQueue()).build();
    }

    @Bean
    Binding bindingNewOrderQueue() {
        return BindingBuilder.bind(newOrderQueue()).to(exchange()).with(properties.newOrdersQueue());
    }

    @Bean
    Queue deliveredOrderQueue() {
        return QueueBuilder.durable(properties.deliveredOrdersQueue()).build();
    }

    @Bean
    Binding bindingDeliveredOrderQueue() {
        return BindingBuilder.bind(deliveredOrderQueue()).to(exchange()).with(properties.deliveredOrdersQueue());
    }

    @Bean
    Queue cancelledOrderQueue() {
        return QueueBuilder.durable(properties.cancelledOrdersQueue()).build();
    }

    @Bean
    Binding bindingCancelledOrderQueue() {
        return BindingBuilder.bind(cancelledOrderQueue()).to(exchange()).with(properties.cancelledOrdersQueue());
    }

    @Bean
    Queue errorOrderQueue() {
        return QueueBuilder.durable(properties.errorOrdersQueue()).build();
    }

    @Bean
    Binding bindingErrorOrderQueue() {
        return BindingBuilder.bind(errorOrderQueue()).to(exchange()).with(properties.errorOrdersQueue());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }
}
