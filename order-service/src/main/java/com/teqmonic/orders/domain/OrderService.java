package com.teqmonic.orders.domain;

import com.teqmonic.orders.domain.models.CreateOrderRequest;
import com.teqmonic.orders.domain.models.CreateOrderResponse;
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
    private final CustomerRepository customerRepository;

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {

        CustomerEntity customerEntity = OrderMapper.buildCustomerEntity(request);
        customerRepository.save(customerEntity);
        log.info("Customer details have been saved to the database");
        // Order
        OrderEntity newOrder = OrderMapper.convertToEntity(request, customerEntity);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderNumber={}", savedOrder.getOrderNumber());
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
