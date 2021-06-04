package com.luizalabs.api.customer.application;

import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerProductsPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;

public interface CustomerBoundary {

    CustomerResponseModel find(long id);
    CustomerResponseModel find(String email);
    CustomerResponseModel create(CustomerPostModel request);
    CustomerResponseModel update(long id, CustomerPutModel request);
    CustomerResponseModel updateFavoriteProducts(long id, CustomerProductsPutModel request);
    void delete(long id);

}
