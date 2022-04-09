package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;


public class CartControllerTest
{
    private CartController cartController;
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp()
    {
        cartController = new CartController(userRepository, cartRepository, itemRepository);

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        user.setId(100);
        user.setUsername("merlin");
        user.setPassword("merlin@123");
        user.setCart(new Cart());
        when(userRepository.findByUsername("merlin")).thenReturn(user);

        Item item = new Item();
        item.setId(100L);
        item.setName("Storage box");
        item.setPrice(BigDecimal.valueOf(10.00));
        item.setDescription("Storage containers");
        when(itemRepository.findById(100L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void addToCartTest()
    {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(100L);
        request.setQuantity(10);
        request.setUsername("merlin");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(100.00), cart.getTotal());
    }

    @Test
    public void removeFromCartTest()
    {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(100L);
        request.setQuantity(5);
        request.setUsername("merlin");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = cartController.removeFromcart(request);
        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(0.00), BigDecimal.valueOf(cart.getTotal().doubleValue()));
    }
}


