package com.luizalabs.api.customer.application;

import java.util.Optional;

import com.luizalabs.api.customer.adapter.jpa.CustomerDataGateway;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPresenter;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerRequestEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseDTO;
import com.luizalabs.api.customer.domain.shared.exception.CustomerEmaiAlreadyRegisteredException;
import com.luizalabs.api.customer.domain.shared.exception.GenericNoContentException;
import com.luizalabs.api.customer.domain.shared.exception.GenericNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerInteractor implements CustomerBoundary {

    private final CustomerDataGateway dataGateway;
    private final CustomerPresenter presenter;

    @Autowired
    public CustomerInteractor(CustomerDataGateway dataGateway, CustomerPresenter presenter) {
        this.dataGateway = dataGateway;
        this.presenter = presenter;
    }

    @Override
    public CustomerResponseDTO find(long id) {
        Optional<CustomerResponseDTO> customer = dataGateway.findById(id);
        return presenter.success(
            customer.orElseThrow(GenericNotFoundException::new)
        );
    }

    @Override
    public CustomerResponseDTO find(String email) {
        Optional<CustomerResponseDTO> customer = dataGateway.findByEmail(email);
        return presenter.success(
            customer.orElseThrow(GenericNotFoundException::new)
        );
    }

    @Override
    public CustomerResponseDTO create(CustomerPostModel request) {
        this.validateEmail(null, request.getEmail());

        CustomerRequestEntity dataRequest = CustomerRequestEntity.builder()
            .name(request.getName())
            .email(request.getEmail())
            .active(true)
            .build();

        CustomerResponseDTO response = dataGateway.save(dataRequest);
        
        return presenter.success(response);
    }

    @Override
    public CustomerResponseDTO update(long id, CustomerPutModel request) {
        Optional<CustomerResponseDTO> actual = dataGateway.findById(id);

        if (actual.isEmpty())
            throw new GenericNoContentException();
        else
            this.validateEmail(actual.get().getEmail(), request.getEmail());

        CustomerRequestEntity dataRequest = CustomerRequestEntity.builder()
            .id(id)
            .name(request.getName())
            .email(request.getEmail())
            .favoritesProducts(request.getFavoritesProducts())
            .build();

        CustomerResponseDTO customer = dataGateway.save(dataRequest);
        return presenter.success(customer);
    }

    @Override
    public void delete(long id) {
        Optional<CustomerResponseDTO> actual = dataGateway.findById(id);

        if (actual.isEmpty())
            throw new GenericNoContentException();

        CustomerRequestEntity dataRequest = CustomerRequestEntity.builder()
            .id(id)
            .active(false)
            .build();

        dataGateway.save(dataRequest);
    }

    private void validateEmail(String actualEmail, String newEmail) {
        if (!newEmail.equalsIgnoreCase(actualEmail) && dataGateway.emailAlreadyExists(newEmail)) {
            throw new CustomerEmaiAlreadyRegisteredException(newEmail);
        }

    }
    
}