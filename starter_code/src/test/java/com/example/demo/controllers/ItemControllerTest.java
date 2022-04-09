package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;


public class ItemControllerTest
{
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp()
    {
        itemController = new ItemController(itemRepository);

        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

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

        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item1));
        when(itemRepository.findByName("Watch")).thenReturn(Collections.singletonList(item2));
    }

    @Test
    public void getItemsTest()
    {
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, items.size());
    }

    @Test
    public void getItemByIdTest()
    {
        ResponseEntity<Item> response = itemController.getItemById(100L);
        Item item = response.getBody();

        assertNotNull(item);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Make up kit", item.getName());
    }

    @Test
    public void getItemsByNameTest()
    {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Watch");
        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, items.size());
        assertEquals(BigDecimal.valueOf(200.00), items.get(0).getPrice());
    }
}
