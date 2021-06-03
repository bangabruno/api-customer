package com.luizalabs.api.customer.domain.shared.exception;

public class GenericNoContentException extends RuntimeException {
    
    public GenericNoContentException() {
        super("Ops! Parece que o recurso solicitado não existe em nossa base e portanto não pode ser atualizado! :(");
    }
}
