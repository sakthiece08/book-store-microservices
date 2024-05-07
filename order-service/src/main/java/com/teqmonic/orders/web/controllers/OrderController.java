package com.teqmonic.orders.web.controllers;

import com.teqmonic.orders.domain.OrderNotFoundException;
import com.teqmonic.orders.domain.OrderService;
import com.teqmonic.orders.domain.SecurityService;
import com.teqmonic.orders.domain.models.CreateOrderRequest;
import com.teqmonic.orders.domain.models.CreateOrderResponse;
import com.teqmonic.orders.domain.models.OrderDTO;
import com.teqmonic.orders.domain.models.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String userName = securityService.getLoginUserName();
        log.info("Creating order for user: {}", userName);
        return orderService.createOrder(userName, request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        String userName = securityService.getLoginUserName();
        log.info("Fetching order summary for user: {}", userName);
        return orderService.findOrders(userName);
    }

    @GetMapping("/{orderNumber}")
    OrderDTO getOrder(@PathVariable(name = "orderNumber") String orderNumber) {
        String userName = securityService.getLoginUserName();
        log.info("Fetching order details for User {} and Order number: {}", userName, orderNumber);
        return orderService
                .findUserOrder(orderNumber, userName)
                .orElseThrow(() -> OrderNotFoundException.forOrderNumber(orderNumber));
    }
}
