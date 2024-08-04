package com.diego.nunez.Prueba.Tecnica.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequestDto {
    private Integer productId;
    private Integer quantityAvailable;
}
