package com.luizalabs.api.customer.domain.model.customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPostModel {
    
    @NotBlank(message = "Nome do cliente deve ser informado!")
    String name;

    @Email(message = "Email do cliente deve possuir um valor válido!")
    @NotBlank(message = "Email do cliente deve ser informado")
    String email;
}
