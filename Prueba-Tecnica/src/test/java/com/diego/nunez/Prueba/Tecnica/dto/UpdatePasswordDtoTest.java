package com.diego.nunez.Prueba.Tecnica.dto;

import org.hibernate.sql.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UpdatePasswordDtoTest {

    @Test
    void updatePasswordDtoOK() {
        String password = "12345";
        String newPassword = "123";

        UpdatePasswordDto updatePassword = new UpdatePasswordDto();
        updatePassword.setPassword(password);
        updatePassword.setNewPassword(newPassword);

        Assertions.assertNotNull(updatePassword);
        Assertions.assertEquals(password, updatePassword.getPassword());
        Assertions.assertEquals(newPassword, updatePassword.getNewPassword());
    }
}
