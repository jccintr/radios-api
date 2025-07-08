package com.jcsoftware.radios.entities.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank(message = "Campo requerido")
		@Email(message="Email inválido",regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
		String email,
		@NotBlank(message = "Campo requerido")
		String password) {

}
