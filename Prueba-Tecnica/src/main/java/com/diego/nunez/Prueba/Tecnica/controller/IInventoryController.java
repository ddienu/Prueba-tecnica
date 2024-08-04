package com.diego.nunez.Prueba.Tecnica.controller;

import com.diego.nunez.Prueba.Tecnica.dto.InventoryRequestDto;
import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface IInventoryController {
    ResponseEntity<Response> getAllInventories();
    ResponseEntity<Response> saveNewInventory(InventoryRequestDto inventoryRequest) throws BadRequestException;
}
