package com.diego.nunez.Prueba.Tecnica.service.auth;

import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameReturnNull(){
        String email = "test@test.com";

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.empty());

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        Assertions.assertNull(result);
    }

    @Test
    void loadUserByUsernameOK(){
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@test.com");
        user.setRole("USER");
        user.setPassword("12345");

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername(user.getEmail());

        Assertions.assertNotNull(result);
    }
}
