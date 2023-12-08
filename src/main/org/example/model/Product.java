package main.org.example.model;

import lombok.*;

@Data
@ToString
@Builder
public class Product  implements Comparable<Product> {
    private Integer id;
    private String name;
    private double price;
    private int stock;

    @Override
    public int compareTo(Product otherProduct) {
        return Double.compare(this.price, otherProduct.price);
    }

}



