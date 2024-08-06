package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.exception.ProductNotFoundException;
import com.diego.nunez.Prueba.Tecnica.repository.IProductRepository;
import com.diego.nunez.Prueba.Tecnica.service.IProductService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    @Autowired
    public ProductServiceImpl (IProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public List<Product> getAllProducts() {
       return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return Optional.ofNullable(productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found")
        ));
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product putProduct(Integer id, Product productToUpdate){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found")
        );
        if( !product.getCategory().equals(productToUpdate.getCategory())){
            product.setCategory(productToUpdate.getCategory());
        }
        if( !product.getName().equals(productToUpdate.getName())){
            product.setName(productToUpdate.getName());
        }
        if( !product.getStock().equals(productToUpdate.getStock())){
            product.setStock(productToUpdate.getStock());
        }
        if( !product.getPrice().equals(productToUpdate.getPrice())){
            product.setPrice(productToUpdate.getPrice());
        }
        if( !product.getDescription().equals(productToUpdate.getDescription())){
            product.setDescription(productToUpdate.getDescription());
        }
        return productRepository.save(product);
    }
    @Override
    public void deleteProduct(Integer id){
        Product productToErase = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not founded")
        );
        productRepository.delete(productToErase);
    }
}
