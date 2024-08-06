package com.diego.nunez.Prueba.Tecnica.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnprocessableOrderExceptionTest {

    @Test
    void inventoryNotFoundExceptionOK(){
        Assertions.assertThrows(UnprocessableOrderException.class, this::methodThatThrowsException);
    }

    void methodThatThrowsException() throws UnprocessableOrderException{
        throw new UnprocessableOrderException("Unprocessable order");
    }
}
