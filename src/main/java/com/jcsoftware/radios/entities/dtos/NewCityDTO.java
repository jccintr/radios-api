package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.enums.State;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewCityDTO(
		@NotBlank(message = "Campo requerido")
		@Size(min = 3, message = "O campo deve ter pelo menos 3 caracteres")
		String name,
		@NotNull(message = "Campo requerido")
		State state) {

}
