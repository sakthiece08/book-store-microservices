package com.teqmonic.orders.clients.catalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductServiceClient {

    private final RestClient restClient;

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodefallBack")
    public Optional<Product> getProductByCode(String code) {
        log.info("Fetching product for code {}", code);

        return Optional.ofNullable(
                restClient.get().uri("/api/v1/products/{code}", code).retrieve().body(Product.class));
    }

    public Optional<Product> getProductByCodefallBack(String code, Throwable t) {
        log.info("getProductByCodefallBack, code: {}", code);
        log.error("Error while fetching ProductByCode from Catalog-service: ", t);
        return Optional.empty();
    }
}
