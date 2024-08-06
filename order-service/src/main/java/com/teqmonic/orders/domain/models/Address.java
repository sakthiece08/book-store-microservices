package com.teqmonic.orders.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank(message = "AddressLine1 is required")  @JsonProperty("address_line1") String addressLine1,
        String addressLine2,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "State is required") String state,
        @NotBlank(message = "ZipCode is required") String zipCode,
        @NotBlank(message = "Country is required") String country) {}
