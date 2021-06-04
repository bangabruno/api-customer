package com.luizalabs.api.customer.domain.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseModel implements Product, Serializable {
    
    private String id;
    private String title;
    private BigDecimal price;
    private String image;
    private String brand;
    private BigDecimal reviewScore;
}
