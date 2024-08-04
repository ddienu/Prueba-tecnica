package com.diego.nunez.Prueba.Tecnica.service;

import com.diego.nunez.Prueba.Tecnica.dto.InventoryRequestDto;
import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface IInventoryService {
    List<Inventory> getAllInventories();
    Inventory getInventoryById(Integer id) throws BadRequestException;
    Inventory saveInventory(InventoryRequestDto inventoryRequest) throws BadRequestException;
    void updateInventory(Integer id, Integer quantityAvailable) throws BadRequestException;
    void removeInventory(Integer id) throws BadRequestException;

}
