package com.luizalabs.api.customer.domain.model.customer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.vladmihalcea.hibernate.type.array.ListArrayType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class CustomerEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "active")
    private boolean active;

    @Type(type = "list-array")
    @Column(name = "favorite_products", columnDefinition = "text[]")
    @Builder.Default
    private List<String> favoriteProducts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
}
