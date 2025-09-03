package com.challange.stocksync.remote.vendorB;

import com.challange.stocksync.api.dto.ProductDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VendorBService {
    @Value("${vendor.b.csv-path:/tmp/vendor-b/stock.csv}")
    private String  csvPath;

    public List<ProductDto> fetchProducts() {
        log.info("Fetching products from vendor B CSV at " + csvPath);
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
            log.info("Product {} from {} added to list", product.getSku(), product.getVendor());
          });
        } catch (Exception e) {
            log.info("Error reading CSV file from vendor B", e);
        }
        return products;
    }
}