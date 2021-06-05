package com.luizalabs.api.customer.adapter.http;

import javax.validation.Valid;

import com.luizalabs.api.customer.application.CustomerBoundary;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerProductsPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseModel;
import com.luizalabs.api.customer.domain.shared.GenericResponse;
import com.luizalabs.api.customer.domain.shared.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerBoundary inbound;
    
    @Autowired
    public CustomerController(CustomerBoundary inbound) {
        this.inbound = inbound;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<CustomerResponseModel>> create(@Valid @RequestBody CustomerPostModel request) {
        GenericResponse<CustomerResponseModel> response = GenericResponse.success(
            inbound.create(request), Messages.CUSTOMER_SUCCESS
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<CustomerResponseModel>> update(
        @PathVariable("id") Long id,
        @Valid @RequestBody CustomerPutModel request) {
        GenericResponse<CustomerResponseModel> response = GenericResponse.success(
            inbound.update(id, request), Messages.CUSTOMER_SUCCESS
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/products/favorites")
    public ResponseEntity<GenericResponse<CustomerResponseModel>> updateFavoriteProducts(
        @PathVariable("id") Long id,
        @Valid @RequestBody CustomerProductsPutModel request) {
        GenericResponse<CustomerResponseModel> response = GenericResponse.success(
            inbound.updateFavoriteProducts(id, request), Messages.CUSTOMER_SUCCESS
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<CustomerResponseModel>> delete(
        @PathVariable("id") Long id) {
        inbound.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponse.success(
            null, Messages.CUSTOMER_SUCCESS)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CustomerResponseModel>> getById(
        @PathVariable("id") Long id) {
        GenericResponse<CustomerResponseModel> response = GenericResponse.success(
            inbound.find(id), Messages.CUSTOMER_SUCCESS
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<CustomerResponseModel>> getByEmail(@RequestParam("email") String email) {
        GenericResponse<CustomerResponseModel> response = GenericResponse.success(
            inbound.find(email), Messages.CUSTOMER_SUCCESS
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
