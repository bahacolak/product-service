package com.bahadircolak.productservice.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private String code;
    private String description;
    private String brand;
    private String currency;
    private double price;
    private int stock;
}
