package com.luizalabs.api.customer.domain.model.customer;

import java.util.Set;

import com.luizalabs.api.customer.domain.model.product.ProductDTO;

public interface Customer {

    Long getId();
    String getName();
    String getEmail();
    boolean isActive();
    Set<ProductDTO> getFavoritesProducts();
}
