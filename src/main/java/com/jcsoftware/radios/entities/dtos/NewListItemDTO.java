package com.jcsoftware.radios.entities.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewListItemDTO(
		@NotNull(message = "Campo requerido")
		@Min(value = 1, message = "ID inválido")
		Long listId,
		@NotNull(message = "Campo requerido")
	    @Min(value = 1, message = "ID inválido")
		Long radioId) {

}
