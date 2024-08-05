package com.diego.nunez.Prueba.Tecnica.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private List<ProductRequest> products;
}
