package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.dto.InventoryRequestDto;
import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.repository.IInventoryRepository;
import com.diego.nunez.Prueba.Tecnica.repository.IProductRepository;
import com.diego.nunez.Prueba.Tecnica.service.IInventoryService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements IInventoryService {

    private final IInventoryRepository inventoryRepository;
    private final IProductRepository productRepository;

    @Autowired
    public InventoryServiceImpl(IInventoryRepository inventoryRepository, IProductRepository productRepository){
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }
    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory saveInventory(InventoryRequestDto inventoryRequest) throws BadRequestException {
        Optional<Product> productFounded = Optional.ofNullable(productRepository.findById(inventoryRequest.getProductId()).orElseThrow(
                () -> new BadRequestException("Product not found")
        ));
        Inventory inventory = new Inventory();
        if( productFounded.isPresent()){
            inventory.setProduct(productFounded.get());
            inventory.setQuantityAvailable(inventoryRequest.getQuantityAvailable());
        }
        return inventoryRepository.save(inventory);
    }
}