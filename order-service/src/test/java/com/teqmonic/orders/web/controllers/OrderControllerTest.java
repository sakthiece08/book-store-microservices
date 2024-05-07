package com.teqmonic.orders.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.teqmonic.orders.AbstractIntegrationTest;
import com.teqmonic.orders.domain.models.OrderSummary;
import com.teqmonic.orders.testdata.TestDataFactory;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
class OrderControllerTest extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTest {
        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P100", "Battery", new BigDecimal(34));
            var payload =
                    """
                    {
                        "customer": {
                            "name": "Tom",
                            "email": "tomemail@test.com",
                            "phone": "416-990-0989"
                        },
                        "deliveryAddress": {
                            "addressLine1": "715-200 Aiphine st",
                            "city": "Toronto",
                            "state": "ON",
                            "zipCode": "M4S1C0",
                            "country": "Canada"
                        },
                        "items": [
                            {
                                "code": "P100",
                                "name": "Battery",
                                "price": 34,
                                "quantity": 2
                            }
                        ]
                    }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            var payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrdersTest {
        @Test
        void shouldGetOrdersSuccessfully() {
            List<OrderSummary> orderSummaries = given().when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertThat(orderSummaries).hasSize(2);
            assertThat(orderSummaries)
                    .allMatch(orderSummary -> orderSummary.orderNumber().contains("ORD-"));
            assertThat(orderSummaries).anyMatch(order -> order.orderNumber().equals("ORD-100"));
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        String orderNumber = "ORD-100";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .body("order_number", is(orderNumber))
                    .body("items.size()", is(2));
        }
    }
}
