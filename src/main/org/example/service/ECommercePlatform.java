package main.org.example.service;

import main.org.example.comparator.ProductNameComparator;
import main.org.example.comparator.ProductStockComparator;
import main.org.example.model.Order;
import main.org.example.model.Product;
import main.org.example.model.User;

import java.util.*;

public class ECommercePlatform {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Product> products = new HashMap<>();
    private final Map<Integer, Order> orders = new HashMap<>();

    private static int userCount = 0;
    private static int productCount = 0;
    private static int orderCount = 0;


    public void addUser(User user) {
        user.setId(++userCount);

        if (users.containsKey(user.getId())) {
            throw new RuntimeException("User already exist!");
        }
        users.put(user.getId(), user);
    }

    public void addUser(User... users) {
        Arrays.stream(users).forEach(this::addUser);
    }


    public void addProduct(Product product) {

        if (!products.containsKey(product.getId())) {
            product.setId(++productCount);
        }
        products.put(product.getId(), product);
    }

    public void addProduct(Product... products) {
        Arrays.stream(products).forEach(this::addProduct);
    }


    public void createOrder(User user) {

        if (user.getCart().isEmpty()) {
            throw new RuntimeException("User cart cant be empty for create order!");
        }

        updateStock(user.getCart());

        for (Map.Entry<Product, Integer> cartEntry : user.getCart().entrySet()) {
            Product product = cartEntry.getKey();
            int quantity = cartEntry.getValue();

            int currentQuantity = user.getHistory().getOrDefault(product, 0);

            user.getHistory().put(product, currentQuantity + quantity);
        }

        Map<Product, Integer> cartCopy = new HashMap<>(user.getCart());
        user.getCart().clear();

        orders.put(++orderCount, Order.builder()
                .id(orderCount)
                .orderDetails(cartCopy)
                .userId(user.getId())
                .totalPrice(Order.calculateTotalPrice(user.getCart()))
                .build());

    }

    private void updateStock(Map<Product, Integer> orderDetails) {
        for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
            Product product = entry.getKey();
            int requestedQuantity = entry.getValue();

            if (!products.containsKey(product.getId())) {
                throw new RuntimeException("product with ID: " + product.getId() + " not found!");
            }

            Product existingProduct = products.get(product.getId());
            int remainingStock = existingProduct.getStock() - requestedQuantity;

            if (remainingStock < 0) {
                throw new RuntimeException("Negative stock not allowed for product with ID: " + product.getId());
            }

            existingProduct.setStock(remainingStock);
        }
    }

    public void showUsers() {
        System.out.println("\nUsers:");
        printMap(users);
    }

    public void showProducts() {
        System.out.println("\nProducts:");
        printMap(products);
    }

    public void showOrders() {
        System.out.println("\nOrders:");
        printMap(orders);
    }

    private <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void displaySortedProductsByName() {
        displaySortedProductsByComparable(new ProductNameComparator(), "Products sorted by name:");
    }

    public void displaySortedProductsByPrice() {
        displaySortedProductsByComparable(Product::compareTo, "Products sorted by price:");
    }

    public void displaySortedProductsByStock() {
        displaySortedProductsByComparable(new ProductStockComparator(), "Products sorted by stock:");
    }

    public void displaySortedProductsByComparable(Comparator<Product> comparator, String msg) {
        System.out.println(msg);
        products.values().stream()
                .sorted(comparator)
                .toList()
                .forEach(System.out::println);
    }

    public void displayAvailableProducts() {
        List<Product> availableProducts = products.values().stream()
                .filter(product -> product.getStock() > 0)
                .toList();
        System.out.println("Available Products:");
        availableProducts.forEach(System.out::println);
    }

    public List<Product> recommendProducts(User user) {
        Map<Product, Integer> userHistory = user.getHistory();

        if (!userHistory.isEmpty()) {
            Product mostPopularProduct = userHistory.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (mostPopularProduct != null) {
                return Collections.singletonList(mostPopularProduct);
            }
        }


        Map<Product, Integer> userCart = user.getCart();

        if (!userCart.isEmpty()) {
            return new ArrayList<>(userCart.keySet());
        }

        return Collections.emptyList();
    }
}
