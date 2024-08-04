package com.diego.nunez.Prueba.Tecnica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User user;
    @ManyToMany
    private List<Product> products;
    private LocalDateTime creationDate;
    private String status;
    private Double total;
}
