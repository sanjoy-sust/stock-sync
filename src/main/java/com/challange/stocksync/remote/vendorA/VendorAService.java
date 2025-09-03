package com.challange.stocksync.remote.vendorA;

import com.challange.stocksync.api.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class VendorAService {
    private final RestTemplate restTemplate;

    @Value("${vendor.a.api-url}")
    private String apiUrl;

    public VendorAService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductDto> fetchProducts() {
        log.info("Fetching products from vendor A"+ apiUrl);
        ProductDto[] products = restTemplate.getForObject(apiUrl, ProductDto[].class);
        return Arrays.stream(products)
                .peek(p -> p.setVendor("VendorA"))
                .toList();
    }
}