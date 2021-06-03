package com.luizalabs.api.customer.domain.shared.exception;

public class GenericNotFoundException extends RuntimeException {
    
    public GenericNotFoundException() {
        super("Ops! Não encontramos o recurso solicitado. :(");
    }
}
