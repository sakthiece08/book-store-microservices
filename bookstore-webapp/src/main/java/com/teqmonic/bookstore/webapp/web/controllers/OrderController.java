package com.teqmonic.bookstore.webapp.web.controllers;

import com.teqmonic.bookstore.webapp.clients.orders.CreateOrderRequest;
import com.teqmonic.bookstore.webapp.clients.orders.OrderConfirmationDTO;
import com.teqmonic.bookstore.webapp.clients.orders.OrderDTO;
import com.teqmonic.bookstore.webapp.clients.orders.OrderServiceClient;
import com.teqmonic.bookstore.webapp.clients.orders.OrderSummary;
import com.teqmonic.bookstore.webapp.services.SecurityHelper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@Slf4j
public class OrderController {

    private final OrderServiceClient orderServiceClient;
    private final SecurityHelper securityHelper;

    @GetMapping("/cart")
    String cart() {
        return "cart";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    OrderConfirmationDTO createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        log.info("Creating order: {}", orderRequest);
        return orderServiceClient.createOrder(getHeaders(), orderRequest);
    }

    @GetMapping("/orders/{orderNumber}")
    String showOrderDetails(@PathVariable String orderNumber, Model model) {
        model.addAttribute("orderNumber", orderNumber);
        return "order_details";
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDTO getOrder(@PathVariable String orderNumber) {
        log.info("Fetching order details for orderNumber: {}", orderNumber);
        OrderDTO orderDTO = orderServiceClient.getOrder(getHeaders(), orderNumber);
        System.out.println(orderDTO);
        return orderDTO;
    }

    @GetMapping("/orders")
    String showOrders() {
        return "orders";
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderSummary> getOrders() {
        log.info("Fetching orders");
        return orderServiceClient.getOrders(getHeaders());
    }

    private Map<String, ?> getHeaders() {
        String accessToken = securityHelper.getAccessToken();
        return Map.of("Authorization", "Bearer " + accessToken);
    }
}
