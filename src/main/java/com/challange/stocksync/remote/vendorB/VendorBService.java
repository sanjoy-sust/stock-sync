package com.challange.stocksync.remote.vendorB;

import com.challange.stocksync.api.dto.ProductDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorBService {
    @Value("${vendor.b.csv-path}")
    private String  csvPath;

    public List<ProductDto> fetchProducts() {
        List<ProductDto> products = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(csvPath), CSVFormat.DEFAULT.withHeader())) {
      parser.forEach(
          record -> {
            ProductDto product = new ProductDto();
            product.setId(Long.parseLong(record.get("sku").hashCode() + "VendorB".hashCode() + ""));
            product.setSku(record.get("sku"));
            product.setName(record.get("name"));
            product.setStockQuantity(Integer.parseInt(record.get("stockQuantity")));
            product.setVendor("VendorB");
            products.add(product);
          });
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file: " + e.getMessage());
        }
        return products;
    }
}