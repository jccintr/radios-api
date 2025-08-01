package com.jcsoftware.radios.entities.dtos;

import java.time.Instant;
import java.util.List;

import com.jcsoftware.radios.entities.RadioList;

public record RadioListDTO(Long id,String name,UserDTO owner,Instant createdAt,Instant updatedAt,List<ListItemDTO> radios) {
	
	public RadioListDTO(RadioList entity) {
		this(
		entity.getId(),
		entity.getName(),
		new UserDTO(entity.getOwner()),
		entity.getCreatedAt(),
		entity.getUpdatedAt(),
		entity.getRadios().stream().map(ListItemDTO::new).toList()
		);
	}
	
}
