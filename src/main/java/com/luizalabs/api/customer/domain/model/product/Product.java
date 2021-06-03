package com.luizalabs.api.customer.domain.model.product;

import java.math.BigDecimal;

public interface Product {
    
    BigDecimal getPrice();
    String getImage();
    String getBrand();
    String getId();
    String getTitle();
    BigDecimal getReviewScore();

}