package com.luizalabs.api.customer.adapter.http;

import javax.validation.Valid;

import com.luizalabs.api.customer.application.CustomerBoundary;
import com.luizalabs.api.customer.domain.model.customer.CustomerPostModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerPutModel;
import com.luizalabs.api.customer.domain.model.customer.CustomerResponseDTO;
import com.luizalabs.api.customer.domain.shared.GenericResponse;
import com.luizalabs.api.customer.domain.shared.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<GenericResponse<CustomerResponseDTO>> create(@Valid @RequestBody CustomerPostModel request) {
        GenericResponse<CustomerResponseDTO> response = GenericResponse.success(inbound.create(request), Messages.CUSTOMER_SUCCESS);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<GenericResponse<CustomerResponseDTO>> update(@PathVariable long id, @Valid @RequestBody CustomerPutModel request) {
        GenericResponse<CustomerResponseDTO> response = GenericResponse.success(inbound.update(id, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<CustomerResponseDTO>> delete(@PathVariable long id) {
        inbound.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponse.success(null, Messages.CUSTOMER_SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CustomerResponseDTO>> get(@PathVariable long id) {
        GenericResponse<CustomerResponseDTO> response = GenericResponse.success(inbound.find(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<GenericResponse<CustomerResponseDTO>> get(@PathVariable String email) {
        GenericResponse<CustomerResponseDTO> response = GenericResponse.success(inbound.find(email));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}