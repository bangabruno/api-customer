package com.luizalabs.api.customer.adapter.http;

import java.util.stream.Collectors;

import com.luizalabs.api.customer.domain.shared.GenericResponse;
import com.luizalabs.api.customer.domain.shared.exception.GenericNotFoundException;
import com.luizalabs.api.customer.domain.shared.exception.GenericUnprocessableEntityException;

import org.apache.commons.compress.utils.Sets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

@ControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class) 
    public ResponseEntity<GenericResponse<?>> handle(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(GenericResponse.fail(Sets.newHashSet(ex.getMessage())));
    }

    @ExceptionHandler(GenericUnprocessableEntityException.class) 
    public ResponseEntity<GenericResponse<?>> handle(GenericUnprocessableEntityException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(GenericResponse.fail(Sets.newHashSet(ex.getMessage())));
    }
    
    @ExceptionHandler(GenericNotFoundException.class) 
    public ResponseEntity<GenericResponse<?>> handle(GenericNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.fail(Sets.newHashSet(ex.getMessage())));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<GenericResponse<?>> handle(MethodNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(GenericResponse.fail(Sets.newHashSet(ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<GenericResponse<?>> handle(MethodArgumentNotValidException ex) {
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(
                GenericResponse.fail(
                    ex.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toSet())
                )
            );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<?>> handle(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            GenericResponse.fail(Sets.newHashSet(ex.getMessage()))
        );
    }
}
