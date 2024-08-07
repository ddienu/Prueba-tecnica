package com.diego.nunez.Prueba.Tecnica.controller.auth;

import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.dto.ResponseDataDto;
import com.diego.nunez.Prueba.Tecnica.dto.UpdatePasswordDto;
import com.diego.nunez.Prueba.Tecnica.dto.UserDto;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.UserServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.auth.CustomUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.sql.SQLIntegrityConstraintViolationException;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private JwtServiceImpl jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void registerUserOK() throws Exception {
        Users user = new Users();
        user.setEmail("test@test.com");
        user.setPassword("DiegoArm@");
        user.setName("Diego Alejandro");
        user.setRole("USER");
        user.setId(1);

        given(userService.register(ArgumentMatchers.any())).willReturn(user);

        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("User created successfully")));
    }

    @Test
    void registerUserBadRequestException() throws Exception {
        Users user = new Users();
        user.setEmail("test@test.com");
        user.setPassword("DiegoArm@");
        user.setName("");
        user.setRole("USER");
        user.setId(1);

        given(userService.register(ArgumentMatchers.any())).willReturn(user);

        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Name field cannot be empty")));
    }

    @Test
    void registerThrowsSQLIntegrityConstraintViolationException() throws Exception{
        Users user = new Users();
        user.setEmail("test@test.com");
        user.setPassword("DiegoArm@");
        user.setName("Diego Alejandro");
        user.setRole("USER");
        user.setId(1);

        given(userService.register(ArgumentMatchers.any())).willThrow(new SQLIntegrityConstraintViolationException("The email has already taken"));

        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("The email has already taken")));

    }

    @Test
    void loginOK() throws Exception{
        UserDto user = new UserDto();
        user.setEmail("test@test.com");
        user.setPassword("DiegoArm@");

        String token = "tokenValid";

        given(userService.login(ArgumentMatchers.any())).willReturn(token);

        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("User login successfully")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.token", CoreMatchers.is(token)));
    }

    @Test
    void loginThrowsBadCredentials() throws Exception{
        given(userService.login(ArgumentMatchers.any()))
                .willThrow((new BadCredentialsException("Incorrect email or password. Please check your credentials")));

        UserDto user = new UserDto();
        user.setEmail("invalid@invalid.com");
        user.setPassword("wrongPassword");

        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Incorrect email or password. Please check your credentials")));
    }
    @Test
    void updatePasswordOk() throws Exception {

        UpdatePasswordDto passwords = new UpdatePasswordDto();
        passwords.setPassword("12345");
        passwords.setNewPassword("123456");

        given(jwtService.getTokenFromRequest(ArgumentMatchers.any())).willReturn("Token");
        given(jwtService.getEmailFromToken(ArgumentMatchers.any())).willReturn("test@test.com");
        willDoNothing().given(userService).updatePassword("test@test.com", passwords);

        ResultActions response = mockMvc.perform(put("/api/auth/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwords))
                .header("Authorization", "Bearer testToken"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Password changed successfully")));
    }

    @Test
    void updatePasswordThrowsHttpMessageNotReadableException() throws Exception{
        String token = "validtoken";
        ResultActions response = mockMvc.perform(put("/api/auth/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Required request body is missing")));
    }
}
