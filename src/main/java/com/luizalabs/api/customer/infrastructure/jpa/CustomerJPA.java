package com.luizalabs.api.customer.infrastructure.jpa;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.luizalabs.api.customer.domain.model.customer.CustomerEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerRequestEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;
import com.luizalabs.api.customer.infrastructure.api.ProductDataGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerJPA implements CustomerDataGateway {

    private final CustomerRepository repository;
    private final ProductDataGateway productGateway;

    @Autowired
    public CustomerJPA(CustomerRepository repository, ProductDataGateway productGateway) {
        this.repository = repository;
        this.productGateway = productGateway;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerResponseModel> findById(long id) {
        Optional<CustomerEntity> entity = repository.findByIdAndActiveTrue(id);
        return Optional.ofNullable(
            entity.isPresent() ? this.mapEntityToResponse(entity.get()) : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerResponseModel> findByEmail(String email) {
        Optional<CustomerEntity> entity = repository.findByEmailAndActiveTrue(email);
        return Optional.ofNullable(
            entity.isPresent() ? this.mapEntityToResponse(entity.get()) : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailAlreadyExists(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public CustomerResponseModel save(CustomerRequestEntity request) {
        CustomerEntity entity = this.mapRequestToEntity(request);
        return this.mapEntityToResponse(
            repository.save(entity)
        );
    }

    @Override
    public boolean deleteLogically(long id) {
        return repository.updateActive(false, id) > 0;
        
    }

    @Override
    @Transactional
    public Optional<CustomerResponseModel> updateCustomer(CustomerRequestEntity request) {
        CustomerEntity entity = this.mapRequestToEntity(request);
        repository.updateCustomer(entity);
        return this.findById(request.getId());
    }

    @Override
    public void addProductsToFavorites(long id, Set<String> productIds) {
        productIds.stream().forEach(productId -> repository.addProductsToFavorites(id, productId));
    }

    @Override
    public void removeProductsFromFavorites(long id, Set<String> productIds) {
        productIds.stream().forEach(
            productId -> repository.removeProductFromFavorites(id, productId)
        );
    }

    private CustomerEntity mapRequestToEntity(CustomerRequestEntity request) {
        return CustomerEntity.builder()
            .id(request.getId())
            .name(request.getName())
            .email(request.getEmail())
            .active(request.isActive())
            .build();
    }

    private CustomerResponseModel mapEntityToResponse(CustomerEntity data) {
        return CustomerResponseModel.builder()
            .id(data.getId())
            .name(data.getName())
            .email(data.getEmail())
            .active(data.isActive())
            .createdAt(data.getCreatedAt())
            .favoritesProducts(
                productGateway.findByIds(
                    data.getFavoriteProducts().stream().collect(Collectors.toSet())
                )
            )
            .build();
    }
    
}
