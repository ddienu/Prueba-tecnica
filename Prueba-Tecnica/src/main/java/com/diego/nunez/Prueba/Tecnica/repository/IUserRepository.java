package com.diego.nunez.Prueba.Tecnica.repository;

import com.diego.nunez.Prueba.Tecnica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
}
