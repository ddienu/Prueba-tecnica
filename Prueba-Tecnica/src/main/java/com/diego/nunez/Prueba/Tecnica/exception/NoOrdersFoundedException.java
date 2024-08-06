package com.diego.nunez.Prueba.Tecnica.exception;

public class NoOrdersFoundedException extends RuntimeException{

    public NoOrdersFoundedException(String message){
        super(message);
    }
}
