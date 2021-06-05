package com.luizalabs.api.customer.infrastructure.api;

import java.util.Set;

import com.luizalabs.api.customer.domain.model.product.ProductResponseModel;

public interface ProductDataGateway {
    
    boolean existsById(String id);
    ProductResponseModel findById(String id);
    Set<ProductResponseModel> findByIds(Set<String> ids);
}
