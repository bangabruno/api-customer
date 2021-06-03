package com.luizalabs.api.customer.domain.model.customer;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.luizalabs.api.customer.domain.model.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPutModel {
    
    @NotBlank(message = "Nome do cliente deve ser informado!")
    String name;

    @Email
    @NotBlank(message = "Email do cliente deve ser informado com um email válido!")
    String email;

    Set<Product> favoritesProducts;
}
