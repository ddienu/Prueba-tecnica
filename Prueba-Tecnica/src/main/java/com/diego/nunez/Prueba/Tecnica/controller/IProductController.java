package com.diego.nunez.Prueba.Tecnica.controller;

import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.dto.ResponseDataDto;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface IProductController {

    ResponseEntity<Response> getAllProducts();
    ResponseEntity<Response> getProductById(Integer id);
    ResponseEntity<Response> saveProduct(Product product);
    ResponseEntity<Response> putProduct(Integer id, Product product) throws BadRequestException;
    ResponseEntity<Response> deleteProduct(Integer id) throws BadRequestException;

}
