package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.dto.InventoryRequestDto;
import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.exception.InventoryNotFoundException;
import com.diego.nunez.Prueba.Tecnica.exception.ProductNotFoundException;
import com.diego.nunez.Prueba.Tecnica.repository.IInventoryRepository;
import com.diego.nunez.Prueba.Tecnica.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class InventoryServiceImplTest {

    @Mock
    private IInventoryRepository inventoryRepository;

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllInventoriesOK(){

        List<Inventory> inventoryList = new ArrayList<>();

        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setProduct( product);
        inventory.setQuantityAvailable(3);

        inventoryList.add(inventory);

        when(inventoryRepository.findAll()).thenReturn(inventoryList);

        List<Inventory> result = inventoryService.getAllInventories();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(inventory, result.get(0));
    }

    @Test
    void getInventoryByIdOk(){
        Integer id = 1;

        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setProduct( product);
        inventory.setQuantityAvailable(3);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));

        Inventory result = inventoryService.getInventoryById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(product, inventory.getProduct());
    }

    @Test
    void getInventoryByIdThrowsException() {
        Integer id = 1;

        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(InventoryNotFoundException.class, () -> {
            inventoryService.getInventoryById(id);
        });
    }

    @Test
    void saveInventoryOK(){
        Integer id = 1;

        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        InventoryRequestDto inventoryRequestDto = new InventoryRequestDto();
        inventoryRequestDto.setProductId(id);
        inventoryRequestDto.setQuantityAvailable(3);

        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setProduct( product);
        inventory.setQuantityAvailable(3);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory inventorySaved = inventoryService.saveInventory(inventoryRequestDto);

        Assertions.assertNotNull(inventorySaved);
        Assertions.assertEquals(product, inventorySaved.getProduct());
    }

    @Test
    void saveInventoryThrowsException(){
        Integer id = 1;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
           inventoryService.saveInventory(new InventoryRequestDto());
        });
    }

    @Test
    void updateInventoryOK(){
        Integer id = 1;
        Integer quantityAvailable = 2;

        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setProduct( new Product());
        inventory.setQuantityAvailable(3);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        inventoryService.updateInventory(id, quantityAvailable);


        Assertions.assertEquals(inventory.getQuantityAvailable(), quantityAvailable);
    }

    @Test
    void updateInventoryThrowsException(){
        Integer id = 1;
        Integer quantityAvailable = 2;

        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(InventoryNotFoundException.class, () -> {
            inventoryService.updateInventory(id, quantityAvailable);
        });
    }

    @Test
    void removeInventoryOK(){
        Integer id = 1;

        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setProduct( product);
        inventory.setQuantityAvailable(3);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));

        inventoryService.removeInventory(id);

        verify(inventoryRepository).delete(inventory);
    }
    @Test
    void removeInventoryThrowsException(){
        Integer id = 1;

        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(InventoryNotFoundException.class, () -> {
            inventoryService.removeInventory(id);
        });
    }





}
