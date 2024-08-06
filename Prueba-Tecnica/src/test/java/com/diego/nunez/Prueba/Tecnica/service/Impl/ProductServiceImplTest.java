package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.exception.ProductNotFoundException;
import com.diego.nunez.Prueba.Tecnica.repository.IProductRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProductsOK(){
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<Product> productList = productService.getAllProducts();

        Assertions.assertNotNull(productList);
    }

    @Test
    void getProductByIdOK(){
        Integer id = 1;

        when(productRepository.findById(id)).thenReturn(Optional.of(new Product()));
        Product product = productService.getProductById(id).get();

        Assertions.assertNotNull(product);
    }

    @Test
    void getProductByIdThrowException(){
        Integer id = 1;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(id);
        });
    }

    @Test
    void saveProductOK(){
        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        Assertions.assertEquals(product, savedProduct);
    }

    @Test
    void putProductOk(){
        Integer id = 1;
        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        Product productToUpdate = new Product();
        productToUpdate.setId(1);
        productToUpdate.setPrice(36.0);
        productToUpdate.setDescription("Descript");
        productToUpdate.setStock(2);
        productToUpdate.setCategory("Foods");
        productToUpdate.setName("Jams");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product productUpdated = productService.putProduct(id, productToUpdate);

        Assertions.assertEquals(product.getStock(), productToUpdate.getStock());
        Assertions.assertEquals(product.getCategory(), productToUpdate.getCategory());
        Assertions.assertEquals(product.getName(), productToUpdate.getName());
        Assertions.assertEquals(product.getPrice(), productToUpdate.getPrice());
        Assertions.assertEquals(product.getDescription(), productToUpdate.getDescription());
    }

    @Test
    void putProductThrowsException(){
        Integer id = 1;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.putProduct(id, new Product());
        });
    }

    @Test
    void deleteProductOk(){
        Integer id = 1;
        Product product = new Product();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.deleteProduct(id);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductThrowsException(){
        Integer id = 1;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(id);
        });
    }
}
