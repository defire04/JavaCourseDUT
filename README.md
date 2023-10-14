# Laboratory work 3 : Testing and Mocking Method Behavior

## Program Functionality

The program offers the following functionalities:

### 1. Entities

Product: This is a class representing a product. It has attributes like id, name, and price, which store information
about the product, such as its unique identifier, name, and price.

Cart: This is a class representing a shopping cart. It stores a list of products that the user has added to the cart. It
has methods for adding and removing products from the cart.

Order: This is a class representing an order. It has attributes like orderId, products, and status, which store
information about the order, including its unique identifier, the list of products included in the order, and the order
status (e.g., "IN_PROGRESS," "SHIPPED," and so on).

### 2. Functionality

Add/remove product from cart.

Make an order from products in the basket.

Get the status of a specific order.

### 3. JUnit testing

Test if products are added/removed correctly from the cart.

Test the functionality of placing an order.

Test the functionality of obtaining order status.

## Description of work

## Phase 1:  Create entities

```java

@Data
public class Cart {
    private List<Product> products = new ArrayList<>();
}
```

```java

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
```

```java
public enum OrderStatus {
    IN_PROGRESS,
    SHIPPED,
    DELIVERED,
    CANCELED
}
```

```java

@Data
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private double price;
}
```

## Phase 2: Add functionality

```java
@Data
public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }
}

```

## Phase 3: Create tests

```java
public class ShoppingCartTest {


    @Test
    public void testAddProductToCart() {
        Cart cart = mock(Cart.class);

        Product productToAdd = mock(Product.class);
        when(productToAdd.getId()).thenReturn(1);
        when(productToAdd.getName()).thenReturn("Product 1");
        when(productToAdd.getPrice()).thenReturn(10.0);

        when(cart.getProducts()).thenReturn(List.of(productToAdd));

        cart.addProduct(productToAdd);

        verify(cart).addProduct(productToAdd);

        assertEquals(1, cart.getProducts().size());
        assertEquals(productToAdd, cart.getProducts().get(0));
    }

    @Test
    public void testRemoveProductFromCart() {
        Cart cart = spy(new Cart());

        Product product1 = Mockito.mock(Product.class);
        Product product2 = Mockito.mock(Product.class);

        when(product1.getId()).thenReturn(1);
        when(product1.getName()).thenReturn("Product 1");
        when(product1.getPrice()).thenReturn(10.0);

        when(product2.getId()).thenReturn(2);
        when(product2.getName()).thenReturn("Product 2");
        when(product2.getPrice()).thenReturn(15.0);

        cart.addProduct(product1);
        cart.addProduct(product2);

        cart.removeProduct(product1);
        verify(cart).removeProduct(product1);

        assertEquals(1, cart.getProducts().size());
        assertEquals(product2, cart.getProducts().get(0));
    }

    @Test
    public void testGetOrderStatus() {
        Order order = mock(Order.class);

        when(order.getOrderId()).thenReturn(1);
        when(order.getStatus()).thenReturn(OrderStatus.DELIVERED);

        assertEquals(OrderStatus.DELIVERED, order.getStatus());

    }
}

```

# Conclusion

The skills and knowledge gained during this lab in the field of testing and mocking methods using JUnit and Moskito are extremely important for improving the quality and stability of the software code. I now have the necessary tools to create advanced test cases and effectively test the functionality of applications, which will help in my future work on software projects.