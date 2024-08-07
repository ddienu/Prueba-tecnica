package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.dto.UpdatePasswordDto;
import com.diego.nunez.Prueba.Tecnica.dto.UserDto;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtServiceImpl jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginThrowsBadCredentialsException(){
        UserDto user = new UserDto();
        user.setEmail("test@test.com");
        user.setPassword("12345");

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.login(user);
        });
    }

    @Test
    void loginIncorrectPassword(){
        UserDto user = new UserDto();
        user.setEmail("test@test.com");
        user.setPassword("12345");

        Users userFound = new Users();
        userFound.setEmail(user.getEmail());
        userFound.setPassword(user.getPassword());

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(userFound));

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.login(user);
        });
    }

    @Test
    void loginSuccessfully(){
        UserDto user = new UserDto();
        user.setEmail("test@test.com");
        user.setPassword("12345");

        Authentication authentication = mock(Authentication.class);

        Users userFound = new Users();
        userFound.setEmail(user.getEmail());
        userFound.setPassword(user.getPassword());

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(userFound));
        when(passwordEncoder.matches(user.getPassword(), userFound.getPassword())).thenReturn(true);
        doReturn(authentication).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        when(jwtService.getToken(user)).thenReturn("jwt-token");

        String token = userService.login(user);

        Assertions.assertNotNull(token);
        Assertions.assertEquals("jwt-token", token);
        verify(userRepository, times(1)).getUserByEmail(user.getEmail());
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        verify(jwtService, times(1)).getToken(user);
    }

    @Test
    void registerThrowException(){
        Users user = new Users();
        user.setEmail("test@test.com");

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(SQLIntegrityConstraintViolationException.class, () -> {
            userService.register(user);
        });
    }

    @Test
    void registerSuccess() throws SQLIntegrityConstraintViolationException {
        Users user = new Users();
        user.setEmail("test@test.com");

        String passwordExpected = "12345";
        String roleExpected = "USER";

        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn(passwordExpected);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        userService.register(user);

        Assertions.assertEquals(passwordExpected, user.getPassword());
        Assertions.assertEquals(roleExpected, user.getRole());
        verify(userRepository, times(1)).getUserByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updatePasswordThrowsException(){
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setPassword("12345");
        updatePasswordDto.setNewPassword("123456");

        String email = "test@test.com";

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.updatePassword(email, updatePasswordDto);
        });
    }

    @Test
    void updatePasswordWithoutMatchException(){
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setPassword("12345");
        updatePasswordDto.setNewPassword("123456");

        String expectedPassword = "1234";
        String email = "test@test.com";

        Users user = new Users();
        user.setRole("USER");
        user.setEmail(email);
        user.setPassword("12345");
        user.setId(1);
        user.setName("Juan");


        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(expectedPassword, user.getPassword())).thenReturn(false);

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.updatePassword(user.getEmail(), updatePasswordDto);
        });
    }

    @Test
    void updatePasswordSuccessfully(){
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setPassword("12345");
        updatePasswordDto.setNewPassword("123456");

        String expectedPassword = "12345";
        String email = "test@test.com";

        Users user = new Users();
        user.setRole("USER");
        user.setEmail(email);
        user.setPassword("12345");
        user.setId(1);
        user.setName("Juan");


        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(expectedPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(updatePasswordDto.getNewPassword())).thenReturn("123456");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        userService.updatePassword(user.getEmail(), updatePasswordDto);

        Assertions.assertEquals(updatePasswordDto.getNewPassword(), user.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}
