package main.org.example.comparator;

import main.org.example.model.Product;

import java.util.Comparator;

public class ProductStockComparator implements Comparator<Product> {
    @Override
    public int compare(Product product1, Product product2) {
        return Integer.compare(product1.getStock(), product2.getStock());
    }
}