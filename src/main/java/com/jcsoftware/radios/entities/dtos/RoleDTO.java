package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.Role;

public record RoleDTO(String authority) {
	
	public RoleDTO(Role entity) {
		this(entity.getAuthority());
	}

}
