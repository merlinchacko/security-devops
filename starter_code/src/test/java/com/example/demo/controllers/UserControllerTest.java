package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;


public class UserControllerTest
{
    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController(userRepository,cartRepository,encoder);

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        user.setId(100);
        user.setUsername("merlin");
        user.setPassword("merlin@123");
        user.setCart(new Cart());

        when(userRepository.findByUsername("merlin")).thenReturn(user);
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("chacko")).thenReturn(null);
    }

    @Test
    public void createUserTest() {
        when(encoder.encode("merlin@123")).thenReturn("encodedPassword");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("merlin");
        request.setPassword("merlin@123");
        request.setConfirmPassword("merlin@123");

        final ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("merlin", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());

    }

    @Test
    public void findByUserNameTest() {
        final ResponseEntity<User> response = userController.findByUserName("merlin");
        User user = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(user);
        assertEquals("merlin", user.getUsername());
    }

    @Test
    public void findByIdTest() {
        final ResponseEntity<User> response = userController.findById(100L);
        User user = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(user);
        assertEquals(100, user.getId());
    }
}
