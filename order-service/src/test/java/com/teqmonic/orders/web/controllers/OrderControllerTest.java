package com.teqmonic.orders.web.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import com.teqmonic.orders.AbstractIntegrationTest;
import com.teqmonic.orders.testdata.TestDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderControllerTest extends AbstractIntegrationTest {

    @Nested
    class createOrderTest {
        @Test
        void shouldCreateOrderSuccessfully() {
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
}
