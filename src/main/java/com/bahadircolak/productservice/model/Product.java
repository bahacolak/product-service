package com.bahadircolak.productservice.model;

import com.bahadircolak.productservice.web.dto.ProductDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String description;
    private String brand;
    private String currency;
    private double price;
    private int stock;
    private boolean deleted = false;

    public ProductDto toDto() {
        return ProductDto.builder()
                .id(id)
                .name(name)
                .code(code)
                .brand(brand)
                .currency(currency)
                .price(price)
                .stock(stock)
                .build();
    }
}

