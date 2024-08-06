package com.diego.nunez.Prueba.Tecnica.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInventoryDto {

    private Integer inventoryId;
    private Integer quantityAvailable;
}
