package com.diego.nunez.Prueba.Tecnica.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void idOk(){
        Integer id = 1;
        Inventory inventory = new Inventory();
        inventory.setId(id);
        Assertions.assertEquals(inventory.getId(), id);
    }
}
