package main.org.example.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class Order {
    private Integer id;
    private Integer userId;
    @Builder.Default
    private Map<Product, Integer> orderDetails = new HashMap<>();
    private double totalPrice;

    public static double calculateTotalPrice(Map<Product, Integer> orderDetails) {
        return orderDetails.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();

    }
}
