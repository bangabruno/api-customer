package com.luizalabs.api.customer.infrastructure.jpa;

import java.util.Optional;
import java.util.Set;

import com.luizalabs.api.customer.domain.model.customer.CustomerRequestEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;

public interface CustomerDataGateway {
    
    Optional<CustomerResponseModel> findById(long id);
    Optional<CustomerResponseModel> findByEmail(String email);
    CustomerResponseModel save(CustomerRequestEntity request);
    Optional<CustomerResponseModel> updateCustomer(CustomerRequestEntity request);
    void addProductsToFavorites(long id, Set<String> productIds);
    void removeProductsFromFavorites(long id, Set<String> productIds);
    boolean emailAlreadyExists(String email);
    boolean deleteLogically(long id);

}