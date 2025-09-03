package com.challange.stocksync.remote.vendorB;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvReadWriteTest {

    static class Product {
        Long id;
        String sku;
        String name;
        Integer stockQuantity;
        String vendor;

        Product(Long id, String sku, String name, Integer stockQuantity, String vendor) {
            this.id = id;
            this.sku = sku;
            this.name = name;
            this.stockQuantity = stockQuantity;
            this.vendor = vendor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Product)) return false;
            Product product = (Product) o;
            return id.equals(product.id) &&
                    sku.equals(product.sku) &&
                    name.equals(product.name) &&
                    stockQuantity.equals(product.stockQuantity) &&
                    vendor.equals(product.vendor);
        }
        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    @Test
    void testCsvReadWriteWhenFileNotEmpty() throws IOException {
        // Create a temp file with existing CSV content
        File tempFile = Files.createTempFile("products", ".csv").toFile();

        try (Writer w = new FileWriter(tempFile)) {
            w.write("id,sku,name,stockQuantity,vendor\n");
            w.write("1,ABC123,Product A,10,VendorA\n");
        }

        // Read existing products
        List<Product> products = new ArrayList<>();
        try (Reader in = new FileReader(tempFile)) {
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : parser) {
                products.add(new Product(
                        Long.parseLong(record.get("id")),
                        record.get("sku"),
                        record.get("name"),
                        Integer.parseInt(record.get("stockQuantity")),
                        record.get("vendor")
                ));
            }
        }
        assertEquals(1, products.size());
        assertEquals("ABC123", products.get(0).sku);

        // Append a new product
        products.add(new Product(2L, "XYZ789", "Product B", 8, "VendorB"));

        // Overwrite file with updated products (including the new one)
        try (Writer out = new FileWriter(tempFile, false)) { // 'false' to overwrite!
            CSVPrinter printer = CSVFormat.DEFAULT.withHeader("id", "sku", "name", "stockQuantity", "vendor").print(out);
            for (Product p : products) {
                printer.printRecord(p.id, p.sku, p.name, p.stockQuantity, p.vendor);
            }
        }

        // Verify file now has both rows
        List<Product> products2 = new ArrayList<>();
        try (Reader in2 = new FileReader(tempFile)) {
            CSVParser parser2 = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in2);
            for (CSVRecord record : parser2) {
                products2.add(new Product(
                        Long.parseLong(record.get("id")),
                        record.get("sku"),
                        record.get("name"),
                        Integer.parseInt(record.get("stockQuantity")),
                        record.get("vendor")
                ));
            }
        }
        assertEquals(2, products2.size());
        assertEquals("XYZ789", products2.get(1).sku);
    }
}