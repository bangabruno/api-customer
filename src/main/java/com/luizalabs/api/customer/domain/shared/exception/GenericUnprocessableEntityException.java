package com.luizalabs.api.customer.domain.shared.exception;

public class GenericUnprocessableEntityException extends RuntimeException {
    
    public GenericUnprocessableEntityException(String message) {
        super(message);
    }

}
