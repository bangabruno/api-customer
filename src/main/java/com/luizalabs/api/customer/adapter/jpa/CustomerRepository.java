package com.luizalabs.api.customer.adapter.jpa;

import java.util.Optional;

import com.luizalabs.api.customer.domain.model.customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByIdAndActiveTrue(long id);
    Optional<CustomerEntity> findByEmailAndActiveTrue(String email);
    boolean existsByEmail(String email);

}
