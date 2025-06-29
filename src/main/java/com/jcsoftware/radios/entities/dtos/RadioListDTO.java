package com.jcsoftware.radios.entities.dtos;

import java.time.Instant;
import java.util.List;

import com.jcsoftware.radios.entities.RadioList;

public record RadioListDTO(String name,UserDTO owner,Instant createdAt,Instant updatedAt,List<ListItemDTO> items) {
	
	public RadioListDTO(RadioList entity) {
		this(
		entity.getName(),
		new UserDTO(entity.getOwner()),
		entity.getCreatedAt(),
		entity.getUpdatedAt(),
		entity.getItems().stream().map(ListItemDTO::new).toList()
		);
	}
	
}
