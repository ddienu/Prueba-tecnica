package com.diego.nunez.Prueba.Tecnica.dto;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UpdateInventoryDtoTest {

    @Test
    void updatedInventoryOK(){
        Integer inventoryId = 1;
        Integer quantityAvailable = 1;

        UpdateInventoryDto updateInventoryDto = new UpdateInventoryDto();
        updateInventoryDto.setInventoryId(inventoryId);
        updateInventoryDto.setQuantityAvailable(quantityAvailable);

        Assertions.assertNotNull(updateInventoryDto);

        Assertions.assertEquals(inventoryId, updateInventoryDto.getInventoryId());
        Assertions.assertEquals(quantityAvailable, updateInventoryDto.getQuantityAvailable());
    }
}
