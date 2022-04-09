package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;


public class OrderControllerTest
{
    private OrderController orderController;
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp()
    {
        orderController = new OrderController(userRepository, orderRepository);

        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        Item item1 = new Item();
        item1.setId(100L);
        item1.setName("Make up kit");
        item1.setPrice(BigDecimal.valueOf(50.50));
        item1.setDescription("Make up kit");

        Item item2 = new Item();
        item2.setId(200L);
        item2.setName("Watch");
        item2.setPrice(BigDecimal.valueOf(200.00));
        item2.setDescription("Samsung smart watch");

        List<Item> items = List.of(item1, item2);

        UserOrder order = new UserOrder();
        order.setItems(items);

        List<UserOrder> userOrders = new ArrayList<>();
        userOrders.add(order);

        User user = new User();
        user.setId(100);
        user.setUsername("merlin");
        user.setPassword("merlin@123");

        Cart cart = new Cart();
        cart.setId(100L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(3000));
        user.setCart(cart);

        when(userRepository.findByUsername("merlin")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);
    }

    @Test
    public void submitTest()
    {
        ResponseEntity<UserOrder> response = orderController.submit("merlin");
        UserOrder order = response.getBody();

        assertNotNull(order);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, order.getItems().size());
    }

    @Test
    public void getOrdersForUserTest()
    {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("merlin");
        List<UserOrder> order = response.getBody();

        assertNotNull(order);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, order.size());
    }
}
