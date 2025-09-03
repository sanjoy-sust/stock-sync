package com.challange.stocksync.api.controller;

import com.challange.stocksync.api.dto.ProductDto;
import com.challange.stocksync.core.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ProductService productService;

  @Test
  void testGetAllProducts() throws Exception {
    ProductDto product = new ProductDto();
    product.setSku("TEST123");
    product.setName("Test Product");
    product.setStockQuantity(10);
    product.setVendor("VendorA");
    productService.saveProduct(product);

    mockMvc
        .perform(get("/products").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
        .andExpect(jsonPath("$.data[0].sku", is("TEST123")))
        .andExpect(jsonPath("$.data[0].name", is("Test Product")))
        .andExpect(jsonPath("$.data[0].stockQuantity", is(10)))
        .andExpect(jsonPath("$.data[0].vendor", is("VendorA")));
  }
}
