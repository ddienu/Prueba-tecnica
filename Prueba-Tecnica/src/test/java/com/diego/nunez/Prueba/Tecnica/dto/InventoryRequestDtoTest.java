package com.diego.nunez.Prueba.Tecnica.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventoryRequestDtoTest {

    @Test
    void productIdOK(){
        Integer productId = 1;
        InventoryRequestDto inventoryRequestDto = new InventoryRequestDto();
        inventoryRequestDto.setProductId(productId);
        Assertions.assertEquals(productId, inventoryRequestDto.getProductId());
    }

    @Test
    void quantityAvailableOK(){
        Integer quantityAvailable = 1;
        InventoryRequestDto inventoryRequestDto = new InventoryRequestDto();
        inventoryRequestDto.setQuantityAvailable(quantityAvailable);
        Assertions.assertEquals(quantityAvailable, inventoryRequestDto.getQuantityAvailable());
    }

}
