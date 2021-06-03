package com.luizalabs.api.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import com.luizalabs.api.customer.adapter.jpa.CustomerDataGateway;
import com.luizalabs.api.customer.adapter.jpa.CustomerJPA;
import com.luizalabs.api.customer.adapter.jpa.CustomerRepository;
import com.luizalabs.api.customer.application.CustomerBoundary;
import com.luizalabs.api.customer.application.CustomerInteractor;
import com.luizalabs.api.customer.config.DataSourceConfig;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPresenter;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseDTO;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponser;
import com.luizalabs.api.customer.domain.shared.exception.CustomerEmaiAlreadyRegisteredException;
import com.luizalabs.api.customer.domain.shared.exception.GenericNotFoundException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {DataSourceConfig.class})
@EnableAutoConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerInteractorTest {
    
    @Autowired
    private CustomerRepository repository;

    private CustomerDataGateway dataGateway;
    private CustomerPresenter presenter;

    private CustomerBoundary inbound;

    private CustomerPostModel post;
    private CustomerPutModel put;

    @BeforeAll
    public void setUp() {
        dataGateway = new CustomerJPA(repository);
        presenter = new CustomerResponser();
        inbound = new CustomerInteractor(dataGateway, presenter);
    }

    @BeforeEach
    public void initialize() {
        post = CustomerPostModel.builder()
            .name("Bruno Henrique da Silva")
            .email("primeiroemail@gmail.com")
            .build();
        
        put = CustomerPutModel.builder()
            .name("Bruno Henrique da Silva")
            .email("segundoemail@gmail.com")
            .favoritesProducts(new HashSet<>())
            .build();
    }

    @Test
    @Order(1)
    @DisplayName("Should create Customer with success")
    public void testShouldCreateCustomerWithSuccess() {
        CustomerResponseDTO response = inbound.create(post);
        assertNotNull(response.getId());
        assertTrue(response.isActive());
    }

    @Test
    @Order(2)
    @DisplayName("Should find customer with success by ID")
    public void testShouldFindCustomerWithSuccessByID() {
        CustomerResponseDTO response = inbound.create(post);
        response = inbound.find(response.getId());
        assertNotNull(response.getId());;
    }

    @Test
    @Order(2)
    @DisplayName("Should find customer with success by email")
    public void testShouldFindCustomerWithSuccessByEmail() {
        CustomerResponseDTO response = inbound.create(post);
        response = inbound.find(response.getEmail());
        assertNotNull(response.getId());;
    }

    @Test
    @Order(3)
    @DisplayName("Should throw Exception for email already registered")
    public void testShouldThrowEmailAlreadyRegisteredOnCreate() {
        inbound.create(post);
        assertThrows(CustomerEmaiAlreadyRegisteredException.class, () -> inbound.create(post));
    }

    @Test
    @Order(4)
    @DisplayName("Should throw Exception for Customer not found by ID")
    public void testShouldThrowCustomerNotFoundForID() {
        assertThrows(GenericNotFoundException.class, () -> inbound.find(999));
    }

    @Test
    @Order(5)
    @DisplayName("Should throw Exception for Customer not found by email")
    public void testShouldThrowCustomerNotFoundForEmail() {
        assertThrows(GenericNotFoundException.class, () -> inbound.find("never@email.com"));
    }

    @Test
    @Order(6)
    @DisplayName("Should update Customer's email with success")
    public void testShouldUpdateCustomersEmailWithSuccess() {
        CustomerResponseDTO customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        CustomerResponseDTO customer2 = inbound.update(customer1.getId(), put);
        assertEquals(customer2.getEmail(), put.getEmail());
    }

    @Test
    @Order(7)
    @DisplayName("Should throw Exception for email already registered")
    public void testShouldThrowEmailAlreadyRegisteredOnUpdate() {
        CustomerResponseDTO customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        post.setName("Outro Nome da Silva");
        post.setEmail("terceiroemail@gmail.com");
        CustomerResponseDTO customer2 = inbound.create(post);
        assertNotNull(customer2.getId());

        put.setEmail(customer2.getEmail());
        long id = customer1.getId();
        assertThrows(CustomerEmaiAlreadyRegisteredException.class, () -> inbound.update(id, put));
    }

}
