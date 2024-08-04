package com.diego.nunez.Prueba.Tecnica.controller.Impl;

import com.diego.nunez.Prueba.Tecnica.controller.IInventoryController;
import com.diego.nunez.Prueba.Tecnica.dto.InventoryRequestDto;
import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.dto.ResponseDataDto;
import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import com.diego.nunez.Prueba.Tecnica.service.IInventoryService;
import com.diego.nunez.Prueba.Tecnica.service.Impl.InventoryServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/v1/inventory")
public class InventoryControllerImpl implements IInventoryController {

    private final InventoryServiceImpl inventoryService;

    public InventoryControllerImpl(InventoryServiceImpl inventoryService){
        this.inventoryService = inventoryService;
    }


    @GetMapping(produces = "application/json")
    @Override
    public ResponseEntity<Response> getAllInventories() {
        List<Inventory> listInventories = inventoryService.getAllInventories();
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Those inventories are founded")
                                .inventories(listInventories)
                                .build()
                ), HttpStatus.OK
        );
    }

    @PostMapping(produces = "application/json")
    @Override
    public ResponseEntity<Response> saveNewInventory(@RequestBody InventoryRequestDto inventoryRequest) throws BadRequestException {
        inventoryService.saveInventory(inventoryRequest);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Inventory saved successfully")
                                .build()
                ), HttpStatus.OK
        );
    }
}
