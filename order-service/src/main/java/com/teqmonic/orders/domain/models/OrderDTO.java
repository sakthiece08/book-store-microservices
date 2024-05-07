package com.teqmonic.orders.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@JsonPropertyOrder({
    "order_number",
    "user",
    "created_at",
    "status",
    "total_amount",
    "customer",
    "delivery_address",
    "created_at"
})
public record OrderDTO(
        @JsonProperty(value = "order_number") String orderNumber,
        String user,
        Set<OrderItem> items,
        Customer customer,
        @JsonProperty(value = "delivery_address") Address deliveryAddress,
        OrderStatusEnum status,
        @JsonInclude(JsonInclude.Include.NON_NULL) String comments,
        @JsonProperty(value = "created_at") LocalDateTime createdAt) {

    // The Request is sent with the value but that value is not accepted in the java class.
    @JsonProperty(value = "total_amount", access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
