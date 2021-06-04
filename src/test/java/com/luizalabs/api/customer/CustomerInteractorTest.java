package com.luizalabs.api.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import com.luizalabs.api.customer.adapter.presenter.CustomerPresenter;
import com.luizalabs.api.customer.adapter.presenter.CustomerResponser;
import com.luizalabs.api.customer.application.CustomerBoundary;
import com.luizalabs.api.customer.application.CustomerInteractor;
import com.luizalabs.api.customer.config.DataSourceConfig;
import com.luizalabs.api.customer.config.ProductProperties;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerProductsPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;
import com.luizalabs.api.customer.domain.shared.exception.CustomerEmaiAlreadyRegisteredException;
import com.luizalabs.api.customer.domain.shared.exception.GenericNotFoundException;
import com.luizalabs.api.customer.infrastructure.api.ProductAPI;
import com.luizalabs.api.customer.infrastructure.api.ProductDataGateway;
import com.luizalabs.api.customer.infrastructure.jpa.CustomerDataGateway;
import com.luizalabs.api.customer.infrastructure.jpa.CustomerJPA;
import com.luizalabs.api.customer.infrastructure.jpa.CustomerRepository;

import org.apache.commons.compress.utils.Sets;
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
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = {DataSourceConfig.class})
@EnableAutoConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerInteractorTest {
    
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ProductProperties properties;

    private CustomerDataGateway customerData;;
    private ProductDataGateway productData;
    private CustomerPresenter presenter;

    private CustomerBoundary inbound;

    private CustomerPostModel post;
    private CustomerPutModel put;
    private CustomerProductsPutModel putProducts;

    @BeforeAll
    public void setUp() {
        productData = new ProductAPI(new RestTemplate(), properties);
        customerData = new CustomerJPA(repository, productData);
        presenter = new CustomerResponser();
        inbound = new CustomerInteractor(customerData, presenter);
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
            .build();

        putProducts = CustomerProductsPutModel.builder()
            .productsToAdd(Sets.newHashSet("1bf0f365-fbdd-4e21-9786-da459d78dd1f", "ee9fc710-8876-c40c-7862-275e237d84a4"))
            .build();
    }

    @Test
    @Order(1)
    @DisplayName("Should create Customer with success")
    public void testShouldCreateCustomerWithSuccess() {
        CustomerResponseModel response = inbound.create(post);
        assertNotNull(response.getId());
        assertTrue(response.isActive());
    }

    @Test
    @Order(2)
    @DisplayName("Should find customer with success by ID")
    public void testShouldFindCustomerWithSuccessByID() {
        CustomerResponseModel response = inbound.create(post);
        response = inbound.find(response.getId());
        assertNotNull(response.getId());;
    }

    @Test
    @Order(3)
    @DisplayName("Should find customer with success by email")
    public void testShouldFindCustomerWithSuccessByEmail() {
        CustomerResponseModel response = inbound.create(post);
        response = inbound.find(response.getEmail());
        assertNotNull(response.getId());;
    }

    @Test
    @Order(4)
    @DisplayName("Should throw Exception for email already registered")
    public void testShouldThrowEmailAlreadyRegisteredOnCreate() {
        inbound.create(post);
        assertThrows(CustomerEmaiAlreadyRegisteredException.class, () -> inbound.create(post));
    }

    @Test
    @Order(5)
    @DisplayName("Should throw Exception for Customer not found by ID")
    public void testShouldThrowCustomerNotFoundForID() {
        assertThrows(GenericNotFoundException.class, () -> inbound.find(999));
    }

    @Test
    @Order(6)
    @DisplayName("Should throw Exception for Customer not found by email")
    public void testShouldThrowCustomerNotFoundForEmail() {
        assertThrows(GenericNotFoundException.class, () -> inbound.find("never@email.com"));
    }

    @Test
    @Order(7)
    @DisplayName("Should update Customer's email with success")
    public void testShouldUpdateCustomersEmailWithSuccess() {
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        CustomerResponseModel customer2 = inbound.update(customer1.getId(), put);
        assertEquals(customer2.getEmail(), put.getEmail());
    }

    @Test
    @Order(8)
    @DisplayName("Should throw Exception for email already registered")
    public void testShouldThrowEmailAlreadyRegisteredOnUpdate() {
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        post.setName("Outro Nome da Silva");
        post.setEmail("terceiroemail@gmail.com");
        CustomerResponseModel customer2 = inbound.create(post);
        assertNotNull(customer2.getId());

        put.setEmail(customer2.getEmail());
        long id = customer1.getId();
        assertThrows(CustomerEmaiAlreadyRegisteredException.class, () -> inbound.update(id, put));
    }

    @Test
    @Order(9)
    @DisplayName("Should delete (logically) Customer with success")
    public void testShouldDeleteLogicallyCustomerWithSuccess() {
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        inbound.delete(customer1.getId());
        long id = customer1.getId();
        assertThrows(GenericNotFoundException.class, () -> inbound.find(id));
    }

    @Test
    @Order(10)
    @DisplayName("Should add products to Customer's favorite list with success")
    public void testShouldAddProductsToCustomersFavoriteListWithSuccess() {
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        inbound.updateFavoriteProducts(customer1.getId(), putProducts);
        customer1 = inbound.find(customer1.getId());
        assertFalse(customer1.getFavoritesProducts().isEmpty());
    }

    @Test
    @Order(11)
    @DisplayName("Should remove products to Customer's favorite list with success")
    public void testShouldRemoveProductsToCustomersFavoriteListWithSuccess() {
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        inbound.updateFavoriteProducts(customer1.getId(), putProducts);

        putProducts.setProductsToRemove(putProducts.getProductsToAdd());
        putProducts.setProductsToAdd(new HashSet<>());

        inbound.updateFavoriteProducts(customer1.getId(), putProducts);

        customer1 = inbound.find(customer1.getId());
        assertTrue(customer1.getFavoritesProducts().isEmpty());
    }

}
