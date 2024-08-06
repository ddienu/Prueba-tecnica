package com.diego.nunez.Prueba.Tecnica.exception;

public class InventoryNotFoundException extends RuntimeException{

    public InventoryNotFoundException(String message){
        super(message);
    }
}
