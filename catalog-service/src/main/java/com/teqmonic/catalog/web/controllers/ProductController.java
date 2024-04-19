package com.teqmonic.catalog.web.controllers;

import com.teqmonic.catalog.domain.PagedRecord;
import com.teqmonic.catalog.domain.Product;
import com.teqmonic.catalog.domain.ProductNotFoundException;
import com.teqmonic.catalog.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return productService
                .findByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
