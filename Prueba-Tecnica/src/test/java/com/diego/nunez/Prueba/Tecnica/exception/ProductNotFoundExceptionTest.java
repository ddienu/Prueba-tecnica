package com.diego.nunez.Prueba.Tecnica.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductNotFoundExceptionTest {

    @Test
    void inventoryNotFoundExceptionOK(){
        Assertions.assertThrows(ProductNotFoundException.class, this::methodThatThrowsException);
    }

    void methodThatThrowsException() throws ProductNotFoundException{
        throw new ProductNotFoundException("Product not found");
    }
}
