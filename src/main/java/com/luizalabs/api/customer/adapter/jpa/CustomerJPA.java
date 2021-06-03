package com.luizalabs.api.customer.adapter.jpa;

import java.util.Optional;

import com.luizalabs.api.customer.domain.model.customer.CustomerEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerRequestEntity;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerJPA implements CustomerDataGateway {

    private final CustomerRepository repository;

    @Autowired
    public CustomerJPA(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerResponseDTO> findById(long id) {
        Optional<CustomerEntity> entity = repository.findByIdAndActiveTrue(id);
        return Optional.ofNullable(
            entity.isPresent() ? this.mapEntityToResponse(entity.get()) : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerResponseDTO> findByEmail(String email) {
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
    public CustomerResponseDTO save(CustomerRequestEntity request) {
        CustomerEntity entity = this.mapRequestToEntity(request);
        return this.mapEntityToResponse(
            repository.save(entity)
        );
    }

    private CustomerEntity mapRequestToEntity(CustomerRequestEntity request) {
        return CustomerEntity.builder()
            .name(request.getName())
            .email(request.getEmail())
            .active(request.isActive())
            .build();
    }

    private CustomerResponseDTO mapEntityToResponse(CustomerEntity data) {
        return CustomerResponseDTO.builder()
            .id(data.getId())
            .name(data.getName())
            .email(data.getEmail())
            .active(data.isActive())
            .createdAt(data.getCreatedAt())
            .favoritesProducts(data.getFavoritesProducts())
            .build();
    }
    
}
