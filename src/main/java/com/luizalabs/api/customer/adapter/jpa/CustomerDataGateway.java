package com.luizalabs.api.customer.adapter.jpa;

import java.util.Optional;

import com.luizalabs.api.customer.domain.model.customer.CustomerRequestEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseDTO;

public interface CustomerDataGateway {
    
    Optional<CustomerResponseDTO> findById(long id);
    Optional<CustomerResponseDTO> findByEmail(String email);
    CustomerResponseDTO save(CustomerRequestEntity request);
    boolean emailAlreadyExists(String email);

}