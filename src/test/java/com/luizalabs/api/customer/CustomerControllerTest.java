package com.luizalabs.api.customer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.luizalabs.api.customer.adapter.http.APIControllerAdvice;
import com.luizalabs.api.customer.adapter.http.CustomerController;
import com.luizalabs.api.customer.application.CustomerBoundary;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerProductsPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;
import com.luizalabs.api.customer.domain.shared.exception.CustomerEmaiAlreadyRegisteredException;

import org.apache.commons.compress.utils.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerBoundary inbound;

    private CustomerPostModel post;
    private CustomerPutModel put;
    private CustomerProductsPutModel productsPut;
    private CustomerResponseModel model;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new CustomerController(inbound)).setControllerAdvice(new APIControllerAdvice()).build();
    }

    @BeforeEach
    public void initialize() {
        post = CustomerPostModel.builder()
            .name("Bruno da Silva")
            .email("email@teste.com")
            .build();

        put = CustomerPutModel.builder()
            .name("Bruno Henrique da Silva")
            .email("email@teste.com")
            .build();

        productsPut = CustomerProductsPutModel.builder()
            .productsToAdd(Sets.newHashSet("1bf0f365-fbdd-4e21-9786-da459d78dd1f", "ee9fc710-8876-c40c-7862-275e237d84a4"))
            .productsToRemove(Sets.newHashSet("1bf0f365-fbdd-4e21-9786-da459d78dd1f"))
            .build();

        
        model = CustomerResponseModel.builder()
            .id(1L)
            .name("Bruno da Silva")
            .email("email@teste.com")
            .active(true)
            .createdAt(LocalDateTime.now())
            .favoritesProducts(new HashSet<>())
            .build();
    }

    @Test
    @DisplayName("Should response with created (201)")
    public void testRequestWithCreated() throws Exception {
        when(inbound.create(post)).thenReturn(model);

        mvc.perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(this.toJson(post))
        )
        .andDo(print())
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should response with invalid email (422)")
    public void testRequestWithInvalidEmail() throws Exception {
        post.setEmail(null);
        mvc.perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(this.toJson(post))
        )
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should response with invalid name (422)")
    public void testRequestWithInvalidName() throws Exception {
        post.setName(null);
        mvc.perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(this.toJson(post))
        )
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should response with emailDuplicated (422)")
    public void testRequestWithEmailDuplicated() throws Exception {
        when(inbound.create(post)).thenThrow(new CustomerEmaiAlreadyRegisteredException(post.getEmail()));

        mvc.perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(this.toJson(post))
        )
        .andDo(print())
        .andExpect(status().isUnprocessableEntity())
        .andReturn();
    }

    @Test
    @DisplayName("Should update customer with success (200)")
    public void testUpdateCustomerWithSuccess() throws Exception {
        mvc.perform(
            put("/api/customers/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(this.toJson(put))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    @DisplayName("Should update customer's favorite list with success (200)")
    public void testUpdateCustomersFavoriteListWithSuccess() throws Exception {
        mvc.perform(
            put("/api/customers/{id}/products/favorites", 1)
                .contentType(MediaType.APPLICATION_JSON).content(this.toJson(productsPut))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    @DisplayName("Should delete customer with success (200)")
    public void testDeleteCustomerWithSuccess() throws Exception {
        mvc.perform(
            delete("/api/customers/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(this.toJson(post))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    }

    private String toJson(Object post) {
        String result = "";
        try {
            result = new ObjectMapper().registerModule(new Jdk8Module()).writeValueAsString(post);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }
    
}
