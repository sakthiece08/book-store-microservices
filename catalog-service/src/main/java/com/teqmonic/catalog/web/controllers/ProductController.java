package com.teqmonic.catalog.web.controllers;

import com.teqmonic.catalog.domain.PagedRecord;
import com.teqmonic.catalog.domain.Product;
import com.teqmonic.catalog.domain.ProductNotFoundException;
import com.teqmonic.catalog.domain.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    private final ProductService productService;

    @GetMapping()
    PagedRecord<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Product> getProductBycode(@PathVariable String code) {
        log.info("In getProductBycode for code: {}", code);
        // sleep();
        return productService
                .findByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }

    private void sleep() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
