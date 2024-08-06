package com.diego.nunez.Prueba.Tecnica.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NoOrdersFoundedExceptionTest {

    @Test
    void inventoryNotFoundExceptionOK(){
        Assertions.assertThrows(NoOrdersFoundedException.class, this::methodThatThrowsException);
    }

    void methodThatThrowsException() throws NoOrdersFoundedException{
        throw new NoOrdersFoundedException("No orders found");
    }
}
