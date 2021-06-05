package com.luizalabs.api.customer.application;

import java.util.Optional;

import com.luizalabs.api.customer.adapter.presenter.CustomerPresenter;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerProductsPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerRequestEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;
import com.luizalabs.api.customer.domain.shared.exception.CustomerEmaiAlreadyRegisteredException;
import com.luizalabs.api.customer.domain.shared.exception.GenericNoContentException;
import com.luizalabs.api.customer.domain.shared.exception.GenericNotFoundException;
import com.luizalabs.api.customer.domain.shared.exception.ProductDoesNotExistInRepository;
import com.luizalabs.api.customer.infrastructure.api.ProductDataGateway;
import com.luizalabs.api.customer.infrastructure.jpa.CustomerDataGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CustomerInteractor implements CustomerBoundary {

    private final CustomerDataGateway customerData;
    private final ProductDataGateway productData;
    private final CustomerPresenter presenter;

    @Autowired
    public CustomerInteractor(CustomerDataGateway customerData, CustomerPresenter presenter, ProductDataGateway productData) {
        this.customerData = customerData;
        this.presenter = presenter;
        this.productData = productData;
    }

    @Override
    public CustomerResponseModel find(long id) {
        Optional<CustomerResponseModel> customer = customerData.findById(id);
        return presenter.success(
            customer.orElseThrow(GenericNotFoundException::new)
        );
    }

    @Override
    public CustomerResponseModel find(String email) {
        Optional<CustomerResponseModel> customer = customerData.findByEmail(email);
        return presenter.success(
            customer.orElseThrow(GenericNotFoundException::new)
        );
    }

    @Override
    public CustomerResponseModel create(CustomerPostModel request) {
        CustomerResponseModel response;

        CustomerRequestEntity dataRequest = CustomerRequestEntity.builder()
            .name(request.getName())
            .email(request.getEmail())
            .active(true)
            .build();

        try {
            response = customerData.save(dataRequest);
        } catch (DataIntegrityViolationException e) {
            throw new CustomerEmaiAlreadyRegisteredException(request.getEmail());
        }

        return presenter.success(response);
    }

    @Override
    public CustomerResponseModel update(long id, CustomerPutModel request) {
        Optional<CustomerResponseModel> response;

        CustomerRequestEntity dataRequest = CustomerRequestEntity.builder()
            .id(id)
            .name(request.getName())
            .email(request.getEmail())
            .build();

        try {
            response = customerData.updateCustomer(dataRequest);
            if (response.isEmpty())
                throw new GenericNoContentException();

        } catch (DataIntegrityViolationException e) {
            throw new CustomerEmaiAlreadyRegisteredException(request.getEmail());
        }
        
        return presenter.success(response.orElse(null));
    }

    @Override
    public void delete(long id) {
        if (!customerData.deleteLogically(id))
            throw new GenericNoContentException();
    }

    @Override
    public CustomerResponseModel updateFavoriteProducts(long id, CustomerProductsPutModel request) {
        request.getProductsToAdd().forEach(
            productId -> {
                if (!productData.existsById(productId))
                    throw new ProductDoesNotExistInRepository(productId);
            }
        );

        customerData.addProductsToFavorites(id, request.getProductsToAdd());
        customerData.removeProductsFromFavorites(id, request.getProductsToRemove());
        return presenter.success(this.find(id));
    }
}