package com.luizalabs.api.customer.domain.model.customer;

import java.util.Set;

import com.luizalabs.api.customer.domain.model.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestEntity {
    
    Long id;
    String name;
    String email;
    boolean active;
    Set<Product> favoritesProducts;
}
