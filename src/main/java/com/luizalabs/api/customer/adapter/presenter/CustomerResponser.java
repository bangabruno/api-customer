package com.luizalabs.api.customer.adapter.presenter;

import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;

import org.springframework.stereotype.Service;

@Service
public class CustomerResponser implements CustomerPresenter {

    @Override
    public CustomerResponseModel success(CustomerResponseModel customer) {
        return customer;
    }
    
}
