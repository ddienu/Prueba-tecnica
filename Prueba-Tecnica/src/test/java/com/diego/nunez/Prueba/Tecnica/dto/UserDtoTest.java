package com.diego.nunez.Prueba.Tecnica.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDtoTest {

    @Test
    void userDtoOK() {
        String email = "test@test.com";
        String password = "12345";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);

        Assertions.assertEquals(email, userDto.getEmail());
        Assertions.assertEquals(password, userDto.getPassword());

        Assertions.assertInstanceOf(String.class, email);
        Assertions.assertInstanceOf(String.class, password);
    }
}
