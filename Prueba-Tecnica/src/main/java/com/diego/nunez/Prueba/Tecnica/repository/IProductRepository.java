package com.diego.nunez.Prueba.Tecnica.repository;

import com.diego.nunez.Prueba.Tecnica.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
}
