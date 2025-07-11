package com.jcsoftware.radios.entities.dtos;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



public record NewRadioDTO(
		@NotBlank(message = "Campo requerido")
		@Size(min = 3, message = "O campo deve ter pelo menos 3 caracteres")
		String name,
		@NotBlank(message = "Campo requerido")
		@Size(min = 3, message = "O campo deve ter pelo menos 3 caracteres")
		String shortName,
		@NotBlank(message = "Campo requerido")
		@Size(min = 8, message = "O campo deve ter pelo menos 8 caracteres")
		String streamUrl,
		@NotBlank(message = "Campo requerido")
		@Size(min = 8, message = "O campo deve ter pelo menos 8 caracteres")
		String imageUrl,
		@NotNull(message = "Campo requerido")
		Boolean hsl,
		@NotNull(message = "Campo requerido")
		@Min(value = 1, message = "ID inválido")
		Long cityId,
		@NotEmpty(message = "Deve ser informado pelo menos uma categoria.")
		List<CategoryDTO> categories) {
	
	public NewRadioDTO(
	        String name,
	        String shortName,
	        String streamUrl,
	        String imageUrl,
	        Boolean hsl,
	        Long cityId
	    ) {
	        this(name, shortName, streamUrl, imageUrl, hsl, cityId, new ArrayList<>());
	    }
}
