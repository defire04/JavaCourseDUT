package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
