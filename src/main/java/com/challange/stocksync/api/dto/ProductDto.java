package com.challange.stocksync.api.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String sku;
    private String name;
    private Integer stockQuantity;
    private String vendor;
}
