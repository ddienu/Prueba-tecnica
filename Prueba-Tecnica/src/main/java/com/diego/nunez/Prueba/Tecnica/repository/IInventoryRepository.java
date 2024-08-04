package com.diego.nunez.Prueba.Tecnica.repository;

import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, Integer> {
}
