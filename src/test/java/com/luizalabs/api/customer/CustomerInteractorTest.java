package com.luizalabs.api.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;

import com.luizalabs.api.customer.adapter.presenter.CustomerPresenter;
import com.luizalabs.api.customer.adapter.presenter.CustomerResponser;
import com.luizalabs.api.customer.application.CustomerBoundary;
import com.luizalabs.api.customer.application.CustomerInteractor;
import com.luizalabs.api.customer.config.datasource.DataSourceConfig;
import com.luizalabs.api.customer.config.properties.ProductProperties;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerProductsPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;
import com.luizalabs.api.customer.domain.shared.exception.GenericNoContentException;
import com.luizalabs.api.customer.domain.shared.exception.GenericNotFoundException;
import com.luizalabs.api.customer.domain.shared.exception.GenericUnprocessableEntityException;
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

@SpringBootTest(classes = {DataSourceConfig.class, ProductProperties.class})
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
        productData = new ProductAPI(properties);
        customerData = new CustomerJPA(repository, productData);
        presenter = new CustomerResponser();
        inbound = new CustomerInteractor(customerData, presenter, productData);
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
        post.setName("Segundo Nome");
        post.setEmail("segundoemail@gmail.com");
        CustomerResponseModel response = inbound.create(post);
        response = inbound.find(response.getId());
        assertNotNull(response.getId());;
    }

    @Test
    @Order(3)
    @DisplayName("Should find customer with success by email")
    public void testShouldFindCustomerWithSuccessByEmail() {
        post.setName("Terceiro Nome");
        post.setEmail("terceiroemail@gmail.com");
        CustomerResponseModel response = inbound.create(post);
        response = inbound.find(response.getEmail());
        assertNotNull(response.getId());;
    }

    @Test
    @Order(4)
    @DisplayName("Should throw Exception for email already registered")
    public void testShouldThrowEmailAlreadyRegisteredOnCreate() {
        post.setName("Quarto Nome");
        post.setEmail("quartoemail@gmail.com");
        inbound.create(post);
        assertThrows(GenericUnprocessableEntityException.class, () -> inbound.create(post));
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
        post.setName("Setimo Nome");
        post.setEmail("setimoemail@gmail.com");
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        put.setEmail("setimoemail2@gmail.com");
        CustomerResponseModel customer2 = inbound.update(customer1.getId(), put);
        assertEquals(customer2.getEmail(), put.getEmail());
    }

    @Test
    @Order(8)
    @DisplayName("Should throw Exception for email already registered on update")
    public void testShouldThrowEmailAlreadyRegisteredOnUpdate() {
        post.setName("Oitavo Nome");
        post.setEmail("oitavoemail@gmail.com");
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        post.setName("Oitavo2 Nome");
        post.setEmail("Oitavo2email@gmail.com");
        CustomerResponseModel customer2 = inbound.create(post);
        assertNotNull(customer2.getId());

        put.setEmail(customer2.getEmail());
        long id = customer1.getId();
        assertThrows(GenericUnprocessableEntityException.class, () -> inbound.update(id, put));
    }

    @Test
    @Order(9)
    @DisplayName("Should delete (logically) Customer with success")
    public void testShouldDeleteLogicallyCustomerWithSuccess() {
        post.setName("Nono Nome");
        post.setEmail("nonoemail@gmail.com");
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
        post.setName("Decimo Nome");
        post.setEmail("decimoemail@gmail.com");
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
        post.setName("Decimo Primeiro Nome");
        post.setEmail("decimoprimeiroemail@gmail.com");
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        inbound.updateFavoriteProducts(customer1.getId(), putProducts);

        putProducts.setProductsToRemove(putProducts.getProductsToAdd());
        putProducts.setProductsToAdd(new HashSet<>());

        inbound.updateFavoriteProducts(customer1.getId(), putProducts);

        customer1 = inbound.find(customer1.getId());
        assertTrue(customer1.getFavoritesProducts().isEmpty());
    }

    @Test
    @Order(12)
    @DisplayName("Should remove only one product From Customer's favorite list with success")
    public void testShouldRemoveOnlyOneProductFromCustomersFavoriteListWithSuccess() {
        post.setName("Decimo Segundo Nome");
        post.setEmail("decimopsegundoemail@gmail.com");
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        inbound.updateFavoriteProducts(customer1.getId(), putProducts);

        putProducts.setProductsToRemove(Collections.singleton(putProducts.getProductsToAdd().iterator().next()));

        inbound.updateFavoriteProducts(customer1.getId(), putProducts);

        customer1 = inbound.find(customer1.getId());
        assertFalse(customer1.getFavoritesProducts().isEmpty());
        assertEquals(1, customer1.getFavoritesProducts().size());
    }

    @Test
    @Order(13)
    @DisplayName("Should throw exception for trying to add a product to favorite list that does not exist in repository")
    public void testShouldThrowExceptionForTryingToAddAProductToFavoriteListThatDoesNotExistInRepository() {
        post.setName("Decimo Terceiro Nome");
        post.setEmail("decimoterceiroemail@gmail.com");
        CustomerResponseModel customer1 = inbound.create(post);
        assertNotNull(customer1.getId());

        putProducts.getProductsToAdd().add("IdInexistente");
        long id = customer1.getId();
        assertThrows(GenericUnprocessableEntityException.class, () -> inbound.updateFavoriteProducts(id, putProducts));
    }

    @Test
    @Order(14)
    @DisplayName("Should throw exception for trying to add a product to favorite list of an inexistent customer")
    public void testShouldThrowExceptionForTryingToAddAProductToFavoriteListTOfAnInexistentCustomer() {
        long id = 999L;
        assertThrows(GenericNotFoundException.class, () -> inbound.updateFavoriteProducts(id, putProducts));
    }

    @Test
    @Order(15)
    @DisplayName("Should throw exception for trying to update an inexistent customer")
    public void testShouldThrowExceptionForTryingToUpdateAnInexistentCustomer() {
        long id = 999L;
        assertThrows(GenericNoContentException.class, () -> inbound.update(id, put));
    }

}
