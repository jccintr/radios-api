package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.User;

public record UserDTO(Long id, String name,String email) {
	public UserDTO(User entity) {
		this(entity.getId(),entity.getName(),entity.getEmail());
	}

}
