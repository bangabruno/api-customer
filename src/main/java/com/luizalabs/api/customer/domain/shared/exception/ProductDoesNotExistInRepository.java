package com.luizalabs.api.customer.domain.shared.exception;

public class ProductDoesNotExistInRepository extends GenericUnprocessableEntityException {
    
    public ProductDoesNotExistInRepository(String id) {
        super(String.format("Ops!, parece que o produto com ID '%s' não existe em nosso repositório! =/", id));
    }
}
