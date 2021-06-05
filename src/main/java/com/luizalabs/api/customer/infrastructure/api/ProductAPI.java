package com.luizalabs.api.customer.infrastructure.api;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.luizalabs.api.customer.config.properties.ProductProperties;
import com.luizalabs.api.customer.domain.model.product.ProductResponseModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductAPI implements ProductDataGateway {

    private RestTemplate restTemplate = new RestTemplate();
    private ProductProperties properties;

    @Autowired
    public ProductAPI(ProductProperties properties) {
        this.properties = properties;
    }

    @Override
    public ProductResponseModel findById(String id) {
        ProductResponseModel response = null;
        
        try {
            response = restTemplate.getForObject(properties.getUrlFindById(), ProductResponseModel.class, id);
        } catch (HttpClientErrorException ex) {
            log.error(
                "The request to product-api has failed! | URL: {} | STATUS: {} | ERROR: {}", 
                properties.getUrlFindById(), ex.getRawStatusCode(), ex.getMessage()
            );
        }

        return response;
    }

    @Override
    public Set<ProductResponseModel> findByIds(Set<String> ids) {
        return ids.stream().map(this::findById).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public boolean existsById(String id) {
        return this.findById(id) != null;
    }
    
}
