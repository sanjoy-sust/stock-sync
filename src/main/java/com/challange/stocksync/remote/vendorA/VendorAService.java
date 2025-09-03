package com.challange.stocksync.remote.vendorA;

import com.challange.stocksync.api.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
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

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public List<ProductDto> fetchProducts() {
        log.info("Fetching products from vendor A"+ apiUrl);
        ProductDto[] products = restTemplate.getForObject(apiUrl, ProductDto[].class);
        return Arrays.stream(products)
                .peek(p -> p.setVendor("VendorA"))
                .toList();
    }
}