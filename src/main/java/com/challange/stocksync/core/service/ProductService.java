package com.challange.stocksync.core.service;

import com.challange.stocksync.api.dto.ProductDto;
import com.challange.stocksync.core.entity.Product;
import com.challange.stocksync.core.mapper.ProductMapper;
import com.challange.stocksync.core.repository.ProductRepository;
import com.challange.stocksync.errorhandler.CommonException;
import com.challange.stocksync.errorhandler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public void saveProduct(ProductDto productDto) {
        Product product = mapper.toEntity(productDto);
        productRepository.save(product);
    }

    public void updateProduct(ProductDto productDto) {
        if (productDto.getId() == null || !productRepository.existsById(productDto.getId())) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_FOUND.getCode(), ErrorCode.RESOURCE_NOT_FOUND.getMessage());
        }
        this.saveProduct(productDto);
    }

    public ProductDto findById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_FOUND.getCode(), ErrorCode.RESOURCE_NOT_FOUND.getMessage());
        }
        return productOpt.map(mapper::toDto).get() ;
    }

    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_FOUND.getCode(), ErrorCode.RESOURCE_NOT_FOUND.getMessage());
        }
        productRepository.deleteById(id);
    }

    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProductDto findBySkuAndVendor(String sku, String vendor) {
        Optional<Product> productOpt = productRepository.findBySkuAndVendor(sku, vendor);
        if (productOpt.isEmpty()) {
            return null;
        }
        return productOpt.map(mapper::toDto).get() ;
    }
}
