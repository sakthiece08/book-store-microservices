package com.teqmonic.catalog.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.teqmonic.catalog.AbstractIntegrationTest;
import com.teqmonic.catalog.domain.Product;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class ProductControllerTest extends AbstractIntegrationTest {

    private static final String API_PATH = "/api/v1/products";

    @Test
    void shouldReturnProducts() {

        given().contentType(ContentType.JSON)
                .when()
                .get(API_PATH)
                .then()
                .statusCode(200)
                .body("data", Matchers.hasSize(10))
                .body("totalElements", Matchers.is(15))
                .body("pageNumber", Matchers.is(1))
                .body("totalPages", Matchers.is(2))
                .body("isFirst", Matchers.is(true))
                .body("isLast", Matchers.is(false))
                .body("hasNext", Matchers.is(true))
                .body("hasPrevious", Matchers.is(false));
    }

    @Test
    void shouldGetProductByCode() {
        Product product = given().contentType(ContentType.JSON)
                .when()
                .get(API_PATH + "/{code}", "P100")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .body()
                .as(Product.class);

        assertThat(product.code()).isEqualTo("P100");
        assertThat(product.name()).isEqualTo("The Hunger Games");
        assertThat(product.description()).isEqualTo("Winning will make you famous. Losing means certain death...");
        assertThat(product.price()).isEqualTo(new BigDecimal("34.0"));
    }

    @Test
    void shouldReturnNotFoundWhenProductCodeNotExists() {
        String code = "invalid_product_code";
        given().contentType(ContentType.JSON)
                .when()
                .get(API_PATH + "/{code}", code)
                .then()
                .statusCode(404)
                .body("status", Matchers.is(404))
                .body("title", Matchers.is("Product Not Found"))
                .body("detail", Matchers.is("Product not found with the code: " + code));
    }
}
