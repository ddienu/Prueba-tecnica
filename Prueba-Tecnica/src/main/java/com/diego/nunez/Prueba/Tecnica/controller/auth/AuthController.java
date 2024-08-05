package com.diego.nunez.Prueba.Tecnica.controller.auth;

import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.dto.ResponseDataDto;
import com.diego.nunez.Prueba.Tecnica.dto.UpdatePasswordDto;
import com.diego.nunez.Prueba.Tecnica.dto.UserDto;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/auth")
public class AuthController {

    private final UserServiceImpl userService;
    private final JwtServiceImpl jwtService;

    @Autowired
    public AuthController(UserServiceImpl userService, JwtServiceImpl jwtService){

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping( path = "/register", produces = "application/json")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody Users user) throws SQLIntegrityConstraintViolationException {
        userService.register(user);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("User created successfully")
                                .build()
                ), HttpStatus.OK
        );
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<Response> login(@Valid @RequestBody UserDto user){
        String token = userService.login(user);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("User login successfully")
                                .token(token)
                                .build()
                ), HttpStatus.OK
        );
    }

    @PutMapping(path = "/updatePassword", produces = "application/json")
    public ResponseEntity<Response> updatePassword(@RequestBody UpdatePasswordDto passwords, HttpServletRequest request){
        String token = jwtService.getTokenFromRequest(request);
        String emailFromToken = jwtService.getEmailFromToken(token);
        userService.updatePassword(emailFromToken, passwords);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Password changed successfully")
                                .build()
                ), HttpStatus.OK
        );
    }
}
