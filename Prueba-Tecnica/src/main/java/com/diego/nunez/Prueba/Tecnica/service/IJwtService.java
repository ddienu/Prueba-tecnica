package com.diego.nunez.Prueba.Tecnica.service;

import com.diego.nunez.Prueba.Tecnica.dto.UserDto;

public interface IJwtService {

    String getToken(UserDto user);
}
