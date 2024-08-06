package com.diego.nunez.Prueba.Tecnica.exception;

public class EmptyProductListException extends RuntimeException{

    public EmptyProductListException(String message){
        super(message);
    }
}
