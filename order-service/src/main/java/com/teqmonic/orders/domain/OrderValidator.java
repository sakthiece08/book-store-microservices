package com.teqmonic.orders.domain;

import com.teqmonic.orders.clients.catalog.Product;
import com.teqmonic.orders.clients.catalog.ProductServiceClient;
import com.teqmonic.orders.domain.models.CreateOrderRequest;
import com.teqmonic.orders.domain.models.OrderItem;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderValidator {

    private final ProductServiceClient productServiceClient;

    void validateOrder(CreateOrderRequest request) {
        Set<OrderItem> orderItem = request.items();
        for (OrderItem item : orderItem) {
            Product product = productServiceClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid order: " + item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                log.error(
                        "Product price not matching, Actual price {}, received price {}",
                        item.price(),
                        product.price());
                throw new InvalidOrderException("Product price not matching");
            }
        }
    }
}
