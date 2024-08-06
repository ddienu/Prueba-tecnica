package com.diego.nunez.Prueba.Tecnica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @Min(value = 1, message = "Price must be greater than 0")
    private Double price;
    @NotBlank(message = "Category cannot be empty")
    private String category;
    @Min(value = 1, message = "Stock value must be greater than 0")
    private Integer stock;
}
