package com.luizalabs.api.customer.infrastructure.jpa;

import java.util.Optional;

import com.luizalabs.api.customer.domain.model.customer.CustomerEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    // Derived Queries
    Optional<CustomerEntity> findByIdAndActiveTrue(long id);
    Optional<CustomerEntity> findByEmailAndActiveTrue(String email);
    boolean existsByEmail(String email);
    
    // Native Queries
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE customers SET active = :active WHERE id = :id ")
    int updateActive(@Param("active") boolean active, @Param("id") long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        nativeQuery = true, 
        value = "UPDATE customers SET name = :#{#c.name}, email = :#{#c.email} WHERE id = :id ")
    int updateCustomer(@Param("c") CustomerEntity c);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        nativeQuery = true,
        value = "UPDATE customers SET favorite_products = array_append(favorite_products, CAST(:productId AS text)) WHERE id = :id "
    )
    void addProductsToFavorites(@Param("id") long id, @Param("productId") String productId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        nativeQuery = true, 
        value = "UPDATE customers SET favorite_products = array_remove(favorite_products, CAST(:productId AS text)) WHERE id = :id "
    )
    void removeProductFromFavorites(@Param("id") long id, @Param("productId") String productId);

}
