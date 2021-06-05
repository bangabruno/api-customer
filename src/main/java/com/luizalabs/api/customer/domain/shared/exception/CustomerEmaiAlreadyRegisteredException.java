package com.luizalabs.api.customer.domain.shared.exception;

public class CustomerEmaiAlreadyRegisteredException extends GenericUnprocessableEntityException {
    
    public CustomerEmaiAlreadyRegisteredException(String email) {
        super(String.format("Ops! E-mail '%s' já cadastrado!", email));
    }
}
