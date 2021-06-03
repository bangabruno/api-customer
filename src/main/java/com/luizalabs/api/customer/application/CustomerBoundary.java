package com.luizalabs.api.customer.application;

import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseDTO;

public interface CustomerBoundary {

    CustomerResponseDTO find(long id);
    CustomerResponseDTO find(String email);
    CustomerResponseDTO create(CustomerPostModel request);
    CustomerResponseDTO update(long id, CustomerPutModel request);
    void delete(long id);

}
