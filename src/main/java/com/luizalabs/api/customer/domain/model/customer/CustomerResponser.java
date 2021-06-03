package com.luizalabs.api.customer.domain.model.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerResponser implements CustomerPresenter {

    @Override
    public CustomerResponseDTO success(CustomerResponseDTO customer) {
        return customer;
    }
    
}
