package com.diego.nunez.Prueba.Tecnica.service;

import com.diego.nunez.Prueba.Tecnica.dto.UpdatePasswordDto;
import com.diego.nunez.Prueba.Tecnica.dto.UserDto;
import com.diego.nunez.Prueba.Tecnica.entity.Users;

import java.sql.SQLIntegrityConstraintViolationException;

public interface IUserService {

    String login (UserDto user);
    Users register(Users user) throws SQLIntegrityConstraintViolationException;
    void updatePassword(String email, UpdatePasswordDto passwords);
}
