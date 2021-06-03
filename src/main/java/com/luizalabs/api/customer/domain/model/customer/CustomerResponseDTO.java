package com.luizalabs.api.customer.domain.model.customer;

import java.time.LocalDateTime;
import java.util.Set;

import com.luizalabs.api.customer.domain.model.product.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO implements Customer {

    Long id;
    String name;
    String email;
    LocalDateTime createdAt;
    boolean active;
    Set<ProductDTO> favoritesProducts;
}
