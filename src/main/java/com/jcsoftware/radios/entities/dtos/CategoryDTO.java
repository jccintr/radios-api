package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDTO(
		Long id,
		@NotBlank(message = "Campo requerido")
		@Size(min = 3, message = "O campo deve ter pelo menos 3 caracteres")
		String name) {
	public CategoryDTO(Category entity) {
		this(
				entity.getId(),
				entity.getName()
				);
	}
}
