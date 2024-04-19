package com.teqmonic.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
@DataJpaTest(
        properties = {
            "spring.test.database.replace=none", // turn off in-memory db
            "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
        })
// @Import(ContainersConfig.class) -- this would start all containers defined here..but here we need only Postgresql, so
// use property 'spring.datasource.url' as defined above
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    // You don't need to test the methods provided by Spring Data JPA.
    // This test is to demonstrate how to write tests for the repository layer.
    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(15);
    }
}
