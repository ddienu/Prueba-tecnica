package com.diego.nunez.Prueba.Tecnica.exception;

import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.dto.ResponseDataDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<Response> inventoryNotFoundExceptionHandler(InventoryNotFoundException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(e.getMessage())
                                .build()
                ), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Response> productNotFoundExceptionHandler(ProductNotFoundException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(e.getMessage())
                                .build()
                ), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Response> expiredJwtExceptionHandler(ExpiredJwtException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Token expired")
                                .build()
                ), HttpStatus.UNAUTHORIZED
        );
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e){
        String message = e.getMessage();
        String [] parts = message.split(":");
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(parts[0])
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Response> signatureExceptionHandler(SignatureException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Invalid token")
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> usernameNotFoundExceptionHandler(BadCredentialsException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(e.getMessage())
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EmptyProductListException.class)
    public ResponseEntity<Response> emptyProductListExceptionHandler(EmptyProductListException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(e.getMessage())
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnprocessableOrderException.class)
    public ResponseEntity<Response> unprocessableOrderExceptionHandler(UnprocessableOrderException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(e.getMessage())
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoOrdersFoundedException.class)
    public ResponseEntity<Response> noOrdersFoundedExceptionHandler(NoOrdersFoundedException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(e.getMessage())
                                .build()
                ), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Response> malformedJwtExceptionHandler(MalformedJwtException e){
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Invalid token")
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }

    //Exception that validates the entity in the request body.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Response> sqlIntegrityExceptionHandler(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("The email has already taken")
                                .build()
                ), HttpStatus.BAD_REQUEST
        );
    }
}
