package com.diego.nunez.Prueba.Tecnica.service;

import com.diego.nunez.Prueba.Tecnica.entity.Product;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer id);
    Product saveProduct(Product product);
    Product putProduct(Integer id, Product productToUpdate) throws BadRequestException;
    void deleteProduct(Integer id) throws BadRequestException;
}
