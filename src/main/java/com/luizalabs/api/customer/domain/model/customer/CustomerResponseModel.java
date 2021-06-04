package com.luizalabs.api.customer.domain.model.customer;

import java.time.LocalDateTime;
import java.util.Set;

import com.luizalabs.api.customer.domain.model.product.ProductResponseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseModel implements Customer {

    Long id;
    String name;
    String email;
    LocalDateTime createdAt;
    boolean active;
    Set<ProductResponseModel> favoritesProducts;
}
