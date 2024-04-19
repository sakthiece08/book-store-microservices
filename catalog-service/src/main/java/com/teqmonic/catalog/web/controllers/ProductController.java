package com.teqmonic.catalog.web.controllers;

import com.teqmonic.catalog.domain.PagedRecord;
import com.teqmonic.catalog.domain.Product;
import com.teqmonic.catalog.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/products")
    PagedRecord<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        return productService.getProducts(pageNo);
    }
}
