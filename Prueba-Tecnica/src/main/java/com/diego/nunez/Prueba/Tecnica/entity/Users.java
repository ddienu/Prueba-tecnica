package com.diego.nunez.Prueba.Tecnica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name field cannot be empty")
    private String name;
    @Column(nullable = false, unique = true)
    @Email(message = "The email is invalid")
    private String email;
    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()-_+=.,;:?]).{8,}$",
            message = "The password must have at least 8 characters, one capital letter and one special character")
    private String password;
    private String role;
}
