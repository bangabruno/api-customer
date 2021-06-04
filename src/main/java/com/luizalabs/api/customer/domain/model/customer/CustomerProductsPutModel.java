package com.luizalabs.api.customer.domain.model.customer;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductsPutModel {

    @Builder.Default
    Set<String> productsToAdd = new HashSet<>();

    @Builder.Default
    Set<String> productsToRemove = new HashSet<>();
}
