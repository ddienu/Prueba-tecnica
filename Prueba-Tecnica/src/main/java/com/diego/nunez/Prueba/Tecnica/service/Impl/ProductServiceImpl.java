package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.entity.Product;
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
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product putProduct(Integer id, Product productToUpdate) throws BadRequestException {
        Product product = productRepository.getReferenceById(id);
        if( product == null ){
            throw new BadRequestException("No product founded");
        }
        if( !product.getCategory().equals(productToUpdate.getCategory())){
            product.setCategory(productToUpdate.getCategory());
        }else if( !product.getName().equals(productToUpdate.getName())){
            product.setName(productToUpdate.getName());
        }else if( !product.getStock().equals(productToUpdate.getStock())){
            product.setStock(productToUpdate.getStock());
        }else if( !product.getPrice().equals(productToUpdate.getPrice())){
            product.setPrice(productToUpdate.getPrice());
        }else if( !product.getDescription().equals(productToUpdate.getDescription())){
            product.setDescription(productToUpdate.getDescription());
        }
        return productRepository.save(product);
    }
    @Override
    public void deleteProduct(Integer id) throws BadRequestException {
        Product productToErase = productRepository.getReferenceById(id);
        if( productToErase == null){
            throw new BadRequestException("Product not founded");
        }
        productRepository.delete(productToErase);
    }
}
