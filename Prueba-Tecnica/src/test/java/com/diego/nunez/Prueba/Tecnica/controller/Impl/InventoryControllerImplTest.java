package com.diego.nunez.Prueba.Tecnica.controller.Impl;

import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.exception.InventoryNotFoundException;
import com.diego.nunez.Prueba.Tecnica.exception.ProductNotFoundException;
import com.diego.nunez.Prueba.Tecnica.service.Impl.InventoryServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.auth.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest(controllers = InventoryControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class InventoryControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryServiceImpl inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtServiceImpl jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private Inventory inventory;

    @BeforeEach
    void setUp(){
        Product product = new Product();
        product.setId(1);
        product.setCategory("Food");
        product.setStock(3);
        product.setPrice(35.0);
        product.setDescription("Food");
        product.setName("Jam");
        inventory = new Inventory();
        inventory.setId(1);
        inventory.setProduct(product);
        inventory.setQuantityAvailable(3);
    }

    @Test
    void getAllInventoriesOK() throws Exception{

        List<Inventory> listInventories = new ArrayList<>();
        listInventories.add(inventory);

        given(inventoryService.getAllInventories()).willReturn(listInventories);

        ResultActions response = mockMvc.perform(get("/v1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listInventories)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Those inventories are founded")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].product.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].product.name", CoreMatchers.is("Jam")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].product.description", CoreMatchers.is("Food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].product.price", CoreMatchers.is(35.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].product.category", CoreMatchers.is("Food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].product.stock", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventories[0].quantityAvailable", CoreMatchers.is(3)));
    }

    @Test
    void getInventoryByIdSuccess() throws Exception{
        given(inventoryService.getInventoryById(ArgumentMatchers.any())).willReturn(inventory);

        ResultActions response = mockMvc.perform(get("/v1/inventory/{id}", inventory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventory)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Inventory with id " + inventory.getId() + " is founded")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.product.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.product.name", CoreMatchers.is("Jam")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.product.description", CoreMatchers.is("Food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.product.price", CoreMatchers.is(35.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.product.category", CoreMatchers.is("Food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.product.stock", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.inventory.quantityAvailable", CoreMatchers.is(3)));
    }

    @Test
    void getInventoryByIdThrowsInventoryNotFoundException() throws Exception{
        given(inventoryService.getInventoryById(ArgumentMatchers.any())).willThrow(new InventoryNotFoundException("Inventory not found"));

        ResultActions response = mockMvc.perform(get("/v1/inventory/{id}", inventory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventory)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Inventory not found")));
    }

    @Test
    void saveNewInventorySuccess() throws Exception{
        given(inventoryService.saveInventory(ArgumentMatchers.any())).willReturn(inventory);

        ResultActions response = mockMvc.perform(post("/v1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventory)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Inventory saved successfully")));
    }
    @Test
    void saveNewInventoryThrowsProductNotFoundException() throws Exception{
        given(inventoryService.saveInventory(ArgumentMatchers.any())).willThrow(new ProductNotFoundException("Product not found"));

        ResultActions response = mockMvc.perform(post("/v1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventory)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Product not found")));
    }
    @Test
    void updateInventorySuccess() throws Exception{
        willDoNothing().given(inventoryService).updateInventory(ArgumentMatchers.any(), ArgumentMatchers.any());

        ResultActions response = mockMvc.perform(put("/v1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventory)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Inventory updated successfully")));
    }
    @Test
    void removeInventorySuccess() throws Exception{
        willDoNothing().given(inventoryService).removeInventory(ArgumentMatchers.anyInt());

        ResultActions response = mockMvc.perform(delete("/v1/inventory/{id}", inventory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventory)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Inventory removed successfully")));
    }
}
