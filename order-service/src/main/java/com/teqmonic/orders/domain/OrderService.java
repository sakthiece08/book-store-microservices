package com.teqmonic.orders.domain;

import com.teqmonic.orders.domain.models.CreateOrderRequest;
import com.teqmonic.orders.domain.models.CreateOrderResponse;
import com.teqmonic.orders.domain.models.OrderStatusEnum;
import com.teqmonic.orders.domain.models.events.OrderCancelledEvent;
import com.teqmonic.orders.domain.models.events.OrderCreatedEvent;
import com.teqmonic.orders.domain.models.events.OrderDeliveredEvent;
import com.teqmonic.orders.domain.models.events.OrderFailedEvent;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final CustomerRepository customerRepository;
    private final OrderEventService orderEventService;
    private final List<String> ORDER_DELIVERABLE_COUNTRIES_LIST = List.of("INDIA", "CANADA");

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        log.info("Inside createOrder for username {}", userName);
        orderValidator.validateOrder(request);
        CustomerEntity customerEntity = OrderMapper.buildCustomerEntity(request);
        customerRepository.save(customerEntity);
        log.info("Customer details have been saved to the database");
        // Order
        OrderEntity newOrder = OrderMapper.convertToEntity(request, customerEntity);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderNumber={}", savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        log.info("OrderCreatedEvent is saved");
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    /**
     *
     */
    public void processNewOrders() {
        List<OrderEntity> newOrders = orderRepository.findByStatus(OrderStatusEnum.NEW);
        log.info("Processing {} new order jobs", newOrders.size());
        for (OrderEntity order : newOrders) process(order);
    }

    private void process(OrderEntity order) {

        try {
            if (isOrderDelivered(order)) {
                log.info("Order number {} got successfully delivered!", order.getOrderNumber());
                order.setStatus(OrderStatusEnum.DELIVERED);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                OrderDeliveredEvent orderDeliveredEvent = OrderEventMapper.buildOrderDeliveredEvent(order);
                orderEventService.save(orderDeliveredEvent);
                log.info("Order number {} processed successfully!", order.getOrderNumber());
            } else {
                log.error(
                        "Order number {} could not be delivered to the country {}",
                        order.getOrderNumber(),
                        order.getCustomer().getAddress().country());
                order.setStatus(OrderStatusEnum.CANCELLED);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                OrderCancelledEvent orderCancelledEvent = OrderEventMapper.buildOrderCancelledEvent(
                        order,
                        "Orders can be delivered only to the following countries: "
                                + String.join(", ", ORDER_DELIVERABLE_COUNTRIES_LIST));
                orderEventService.save(orderCancelledEvent);
                log.info(
                        "Order number {} processed with status: {}", order.getOrderNumber(), OrderStatusEnum.CANCELLED);
            }
        } catch (RuntimeException ex) {
            log.error("Error in processing order number {} with exception {}", order.getOrderNumber(), ex.getMessage());
            order.setStatus(OrderStatusEnum.FAILED);
            OrderFailedEvent orderFailedEvent = OrderEventMapper.buildOrderFailedEvent(order, ex.getMessage());
            orderEventService.save(orderFailedEvent);
            log.info("Order number {} processed with status: {}", order.getOrderNumber(), OrderStatusEnum.FAILED);
        }
    }

    /**
     * To simulate real-time order delivery mechanism
     *
     * @param order
     * @return
     */
    private boolean isOrderDelivered(OrderEntity order) {
        return ORDER_DELIVERABLE_COUNTRIES_LIST.contains(
                order.getCustomer().getAddress().country().toUpperCase());
    }
}
