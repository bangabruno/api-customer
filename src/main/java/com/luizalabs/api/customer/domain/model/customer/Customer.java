package com.luizalabs.api.customer.domain.model.customer;

import java.util.Set;

import com.luizalabs.api.customer.domain.model.product.ProductResponseModel;

public interface Customer {

    Long getId();
    String getName();
    String getEmail();
    boolean isActive();
    Set<ProductResponseModel> getFavoritesProducts();
}
