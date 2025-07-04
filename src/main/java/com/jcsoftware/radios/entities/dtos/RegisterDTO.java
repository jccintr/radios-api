package com.jcsoftware.radios.entities.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
		@NotBlank(message = "Campo requerido")
		@Size(min = 3, message = "O campo deve ter pelo menos 3 caracteres")
		String name,
		@NotBlank(message = "Campo requerido")
		@Email(message="Email inválido",regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
		String email,
		@NotBlank(message = "Campo requerido")
		@Size(min = 6, message = "O campo deve ter pelo menos 6 caracteres")
		String password) {

}
