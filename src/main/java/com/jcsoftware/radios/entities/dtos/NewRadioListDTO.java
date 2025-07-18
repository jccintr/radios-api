package com.jcsoftware.radios.entities.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewRadioListDTO(
		@NotBlank(message = "Campo requerido")
		@Size(min = 3, message = "O campo deve ter pelo menos 3 caracteres")
		String name
		) {
	
	public NewRadioListDTO(String name) {
		this.name = name;
		
	}

}
