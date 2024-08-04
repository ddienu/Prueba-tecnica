package com.diego.nunez.Prueba.Tecnica.repository;


import com.diego.nunez.Prueba.Tecnica.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer> {
}
