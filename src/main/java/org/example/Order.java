package org.example;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private int orderId;
    private List<Product> products;
    private OrderStatus status;

    public Order(int orderId, List<Product> products) {
        this.orderId = orderId;
        this.products = products;
        this.status = OrderStatus.IN_PROGRESS;
    }
}
