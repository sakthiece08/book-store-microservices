package com.teqmonic.orders.domain;

public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException() {}

    public InvalidOrderException(String message) {
        super(message);
    }
}
