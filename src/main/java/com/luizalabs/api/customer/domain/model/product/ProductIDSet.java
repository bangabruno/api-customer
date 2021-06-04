package com.luizalabs.api.customer.domain.model.product;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductIDSet implements Serializable {
    
    private Set<String> productIDs;
}
