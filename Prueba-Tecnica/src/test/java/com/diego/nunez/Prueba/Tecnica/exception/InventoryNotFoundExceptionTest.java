package com.diego.nunez.Prueba.Tecnica.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventoryNotFoundExceptionTest {

    @Test
    void inventoryNotFoundExceptionOK(){
        Assertions.assertThrows(InventoryNotFoundException.class, this::methodThatThrowsException);
    }

    void methodThatThrowsException() throws InventoryNotFoundException{
        throw new InventoryNotFoundException("Inventory not found");
    }
}
