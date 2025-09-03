package com.challange.stocksync.core.service;

import com.challange.stocksync.api.dto.ProductDto;
import com.challange.stocksync.core.entity.OutOfStockEvent;
import com.challange.stocksync.core.repository.OutOfStockEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutOfStockEventService {
    private final OutOfStockEventRepository outOfStockEventRepository;

    public void saveEvent(OutOfStockEvent event) {
        outOfStockEventRepository.save(event);
    }
    public void checkStockStatusAndCreateEvent(ProductDto newProduct, ProductDto existingProduct) {
        if (existingProduct != null && existingProduct.getStockQuantity() > 0 && newProduct.getStockQuantity() == 0) {
            OutOfStockEvent event = new OutOfStockEvent();
            event.setSku(newProduct.getSku());
            event.setVendor(newProduct.getVendor());
            event.setTimestamp(LocalDateTime.now());
            this.saveEvent(event);
            log.info("Product {} from {} is out of stock", newProduct.getSku(), newProduct.getVendor());
        }
    }
}
