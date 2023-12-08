package main.org.example;


import main.org.example.model.Product;
import main.org.example.model.User;
import main.org.example.service.ECommercePlatform;

import java.util.stream.IntStream;

public class ECommerceDemo {
    public static void main(String[] args) {

        ECommercePlatform eCommercePlatform = new ECommercePlatform();

        User user1 = User.builder().username("user1").build();
        User user2 = User.builder().username("user2").build();
        eCommercePlatform.addUser(user1, user2);

        Product product1 = Product.builder().name("Apple").price(1.0).stock(100).build();
        Product product2 = Product.builder().name("Banana").price(0.5).stock(150).build();
        Product product3 = Product.builder().name("Bread").price(2.0).stock(50).build();
        Product product4 = Product.builder().name("Cheese").price(5.0).stock(30).build();
        Product product5 = Product.builder().name("Chicken").price(8.0).stock(20).build();
        Product product6 = Product.builder().name("Eggs").price(1.5).stock(40).build();
        Product product7 = Product.builder().name("Milk").price(2.5).stock(60).build();
        Product product8 = Product.builder().name("Yogurt").price(1.8).stock(50).build();
        Product product9 = Product.builder().name("Tomato").price(1.2).stock(70).build();
        Product product10 = Product.builder().name("Pasta").price(2.3).stock(40).build();

        eCommercePlatform.addProduct(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10);

        user1.addToCart(product1, 3);
        user1.addToCart(product2, 2);
        user2.addToCart(product1, 1);

        eCommercePlatform.showUsers();
        eCommercePlatform.showProducts();

        try {
            eCommercePlatform.createOrder(user1);
            eCommercePlatform.createOrder(user2);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n---------------------\nBefore orders:");
        eCommercePlatform.showUsers();
        eCommercePlatform.showProducts();
        eCommercePlatform.showOrders();

        System.out.println("\n---------------------\nSort product");

        eCommercePlatform.showProducts();

        eCommercePlatform.displaySortedProductsByName();
        eCommercePlatform.displaySortedProductsByPrice();
        eCommercePlatform.displaySortedProductsByStock();

        eCommercePlatform.displayAvailableProducts();

        System.out.println("\n---------------------\nRecommendProducts");
        eCommercePlatform.recommendProducts(user1).forEach(System.out::println);
    }

}
