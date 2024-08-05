package com.diego.nunez.Prueba.Tecnica.repository;

import com.diego.nunez.Prueba.Tecnica.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> getUserByEmail(String email);
}
