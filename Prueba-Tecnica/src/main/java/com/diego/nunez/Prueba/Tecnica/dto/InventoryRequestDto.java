package com.diego.nunez.Prueba.Tecnica.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequestDto {
    private Integer productId;
    @Min(value = 1, message = "Quantity available must be greater than 0")
    private Integer quantityAvailable;
}
