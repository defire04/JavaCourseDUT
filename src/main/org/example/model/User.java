package main.org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class User {
    private Integer id;
    private String username;
    @Builder.Default
    private Map<Product, Integer> cart = new HashMap<>();
    @Builder.Default
    private Map<Product, Integer> history = new HashMap<>();


    public void addToCart(Product product, int quantity) {
        cart.put(product, cart.getOrDefault(product, 0) + quantity);
    }

    public void removeFromCart(Product product, int quantity) {
        if (cart.containsKey(product)) {
            int updatedQuantity = cart.get(product) - quantity;
            if (updatedQuantity > 0) {
                cart.put(product, updatedQuantity);
            } else {
                cart.remove(product);
            }
        }
    }

    public void modifyCart(Product product, int newQuantity) {
        if (cart.containsKey(product) && newQuantity > 0) {
            cart.put(product, newQuantity);
        }
    }
}