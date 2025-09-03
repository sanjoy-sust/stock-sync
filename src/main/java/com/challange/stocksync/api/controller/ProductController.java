package com.challange.stocksync.api.controller;
import com.challange.stocksync.api.dto.ApiResponse;
import com.challange.stocksync.api.dto.ProductDto;
import com.challange.stocksync.core.entity.Product;
import com.challange.stocksync.core.repository.ProductRepository;
import com.challange.stocksync.core.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products with their latest stock levels")
    public ApiResponse<?> getAllProducts() {
        List<ProductDto> productList = productService.findAll();
        return ApiResponse.builder()
            .data(productList)
            .build();
    }
}