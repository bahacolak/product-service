package com.bahadircolak.productservice.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String brand;
    private String currency;
    private double price;
    private int stock;
}
