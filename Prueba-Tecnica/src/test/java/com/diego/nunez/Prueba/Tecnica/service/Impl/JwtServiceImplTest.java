package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.dto.UserDto;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.*;

import static org.mockito.Mockito.*;

public class JwtServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTokenThrowException(){
        Map<String, Object> extraClaims = new HashMap<>();
        UserDto user = new UserDto();
        user.setEmail("test@test.com");
        user.setPassword("12345");

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            jwtService.getToken(extraClaims, user);
        });
    }
}
