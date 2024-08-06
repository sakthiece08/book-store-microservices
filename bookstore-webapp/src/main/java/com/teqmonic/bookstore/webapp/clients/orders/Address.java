package com.teqmonic.bookstore.webapp.clients.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record Address(
        @NotBlank(message = "AddressLine1 is required")  @JsonProperty("address_line1") String addressLine1,
        String addressLine2,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "State is required") String state,
        @NotBlank(message = "ZipCode is required") String zipCode,
        @NotBlank(message = "Country is required") String country)
        implements Serializable {}
