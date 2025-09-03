package com.challange.stocksync.core.scheduler;

import com.challange.stocksync.api.dto.ProductDto;
import com.challange.stocksync.core.service.OutOfStockEventService;
import com.challange.stocksync.core.service.ProductService;
import com.challange.stocksync.remote.vendorA.VendorAService;
import com.challange.stocksync.remote.vendorB.VendorBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockSyncService {
    private final VendorAService vendorAService;
    private final VendorBService vendorBService;
    private final ProductService productService;
    private final OutOfStockEventService outOfStockEventService;

    @Scheduled(cron = "${scheduling.cron}")
    @Transactional
    public void syncStock() {
        log.info("Starting stock synchronization");
        syncVendorProducts(vendorAService.fetchProducts());
        syncVendorProducts(vendorBService.fetchProducts());
        log.info("Stock synchronization completed");
    }

    @Transactional
    private void syncVendorProducts(List<ProductDto> products) {
        log.info("Syncing {} products", products.size());
        for (ProductDto newProduct : products) {
            ProductDto existingProduct = productService.findBySkuAndVendor(newProduct.getSku(), newProduct.getVendor());
            outOfStockEventService.checkStockStatusAndCreateEvent(newProduct, existingProduct);
            productService.saveProduct(newProduct);
        }
        log.info("Finished syncing {} products", products.size());
    }
}