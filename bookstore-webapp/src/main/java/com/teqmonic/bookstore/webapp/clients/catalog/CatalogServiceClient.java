package com.teqmonic.bookstore.webapp.clients.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface CatalogServiceClient {

    @GetExchange("/catalog/api/v1/products")
    PagedResult<Product> getProducts(@RequestParam int page);

    @GetExchange("/catalog/api/v1/products/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code);
}
