package com.diego.nunez.Prueba.Tecnica.controller.Impl;

import com.diego.nunez.Prueba.Tecnica.controller.IProductController;
import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.dto.ResponseDataDto;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.service.Impl.ProductServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/v1/product")
public class ProductControllerImpl implements IProductController {

    private final ProductServiceImpl productService;

    @Autowired
    public ProductControllerImpl(ProductServiceImpl productService){
        this.productService = productService;
    }
    @GetMapping(produces = "application/json")
    @Override
    public ResponseEntity<Response> getAllProducts() {
        List<Product> productsFounded = productService.getAllProducts();
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Those products are founded")
                                .products(productsFounded)
                                .build()
                ), HttpStatus.OK
        );
    }

    @GetMapping(path = "{id}", produces = "application/json")
    @Override
    public ResponseEntity<Response> getProductById(@PathVariable Integer id) {
        Product productFounded = productService.getProductById(id).get();
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Product with id " + id + " founded")
                                .product(productFounded)
                                .build()
                ), HttpStatus.OK
        );
    }

    @PostMapping(produces = "application/json")
    @Override
    public ResponseEntity<Response> saveProduct(@Valid @RequestBody Product product) {
        productService.saveProduct(product);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Product save successfully")
                                .build()
                ), HttpStatus.OK
        );
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @Override
    public ResponseEntity<Response> putProduct(@PathVariable Integer id, @RequestBody @Valid Product product) throws BadRequestException {
        productService.putProduct(id, product);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Product updated successfully")
                                .build()
                ), HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    @Override
    public ResponseEntity<Response> deleteProduct(@PathVariable Integer id) throws BadRequestException {
        productService.deleteProduct(id);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Product deleted successfully")
                                .build()
                ), HttpStatus.OK
        );

    }
}
