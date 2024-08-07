package com.diego.nunez.Prueba.Tecnica.dto;

import com.diego.nunez.Prueba.Tecnica.entity.Inventory;
import com.diego.nunez.Prueba.Tecnica.entity.Order;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDataDto implements Serializable {

    private String message;
    private List<Product> products;
    private Product product;
    private List<Inventory> inventories;
    private Inventory inventory;
    private String token;
    private List<Order> orders;

}
