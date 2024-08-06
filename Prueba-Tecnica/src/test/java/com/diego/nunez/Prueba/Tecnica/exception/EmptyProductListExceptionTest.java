package com.diego.nunez.Prueba.Tecnica.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmptyProductListExceptionTest {

    @Test
    void emptyProductListExceptionOK(){
        Assertions.assertThrows(EmptyProductListException.class, this::someMethodThatThrowsException);
    }

    private void someMethodThatThrowsException() throws EmptyProductListException {
        throw new EmptyProductListException("Empty product list");
    }
}
