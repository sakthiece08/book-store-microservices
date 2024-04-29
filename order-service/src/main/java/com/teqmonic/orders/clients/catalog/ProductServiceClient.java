package com.teqmonic.orders.clients.catalog;

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

    public Optional<Product> getProductByCode(String code) {
        log.info("client {}", restClient);
        log.info("Fetching product for code {}", code);
        try {
            return Optional.ofNullable(
                    restClient.get().uri("/api/v1/products/{code}", code).retrieve().body(Product.class));
        } catch (Exception ex) {
            log.error("Error fetching product for code: {}", code, ex);
            return Optional.empty();
        }
    }
}
