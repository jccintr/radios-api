package com.jcsoftware.radios.entities.dtos;

import java.util.List;

import com.jcsoftware.radios.entities.User;

public record UserWithRolesDTO(Long id, String name,String email,List<RoleDTO> roles) {

	public UserWithRolesDTO(User entity) {
		this(
			entity.getId(),
			entity.getName(),
			entity.getEmail(),
			entity.getRoles().stream().map(RoleDTO::new).toList()
			);
	}
}
