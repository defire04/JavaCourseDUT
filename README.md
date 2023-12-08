# Laboratory work 7

## Program Functionality

As part of this project, a basic backend for e-commerce was developed using Java Collections. Key functionalities include inventory management, user cart management, and order processing.

## Phase 1: Class design
```java
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
```

## Phase 2: Electronic Commerce Platform

```java

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


```
## Phase 3: Advanced Functions

```java
public class ProductNameComparator implements Comparator<Product> {
    @Override
    public int compare(Product product1, Product product2) {
        return product1.getName().compareTo(product2.getName());
    }
}

public class ProductStockComparator implements Comparator<Product> {
    @Override
    public int compare(Product product1, Product product2) {
        return Integer.compare(product1.getStock(), product2.getStock());
    }
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
```

## Phase 4: Demonstration
```java
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

```


# Conclusion

A simple e-commerce backend was developed using Java Collections. The system includes management of goods, users and orders. The Product, User, and Order classes interact with each other, allowing you to perform operations such as adding products to the cart, creating orders, and tracking product inventory. The system is ready to use to demonstrate basic e-commerce functionality.