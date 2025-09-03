package com.challange.stocksync.remote.mock;

import com.challange.stocksync.core.entity.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-a")
public class VendorAMockController {
    @GetMapping("/products")
    public List<Product> getProducts() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setSku("ABC123");
        p1.setName("Product A");
        p1.setStockQuantity(8);
        p1.setVendor("VendorA");

        Product p2 = new Product();
        p2.setId(2L);
        p2.setSku("LMN789");
        p2.setName("Product C");
        p2.setStockQuantity(0);
        p2.setVendor("VendorA");

        return List.of(p1, p2);
    }
}