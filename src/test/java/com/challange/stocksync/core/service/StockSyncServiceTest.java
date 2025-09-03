package com.challange.stocksync.core.service;

import com.challange.stocksync.api.dto.ProductDto;
import com.challange.stocksync.core.scheduler.StockSyncService;
import com.challange.stocksync.remote.vendorA.VendorAService;
import com.challange.stocksync.remote.vendorB.VendorBService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = "vendor.b.csv-path=/tmp/test.csv")
class StockSyncServiceTest {

    @Autowired
    private StockSyncService stockSyncService;

    @MockBean
    private VendorAService vendorAService;

    @MockBean
    private VendorBService vendorBService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OutOfStockEventService productEventService;

    @Test
    void testSyncStock() {
        // given
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setSku("ABC123");
        product.setName("Product A");
        product.setStockQuantity(0);
        product.setVendor("VendorA");

        when(vendorAService.fetchProducts()).thenReturn(List.of(product));
        when(vendorBService.fetchProducts()).thenReturn(List.of());

        when(productService.findAll()).thenReturn(List.of(product));

        stockSyncService.syncStock();

        verify(productService, times(1)).saveProduct(product);
        List<ProductDto> products = productService.findAll();
        assertEquals(1, products.size());
        assertEquals("ABC123", products.get(0).getSku());
    }

    @Test
    void testSyncStockVendorB() {
        // given
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setSku("XYZ123");
        product.setName("Product A");
        product.setStockQuantity(0);
        product.setVendor("VendorB");

        when(vendorAService.fetchProducts()).thenReturn(List.of());
        when(vendorBService.fetchProducts()).thenReturn(List.of(product));

        when(productService.findAll()).thenReturn(List.of(product));

        stockSyncService.syncStock();

        verify(productService, times(1)).saveProduct(product);
        List<ProductDto> products = productService.findAll();
        assertEquals(1, products.size());
        assertEquals("XYZ123", products.get(0).getSku());
    }
}
