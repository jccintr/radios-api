package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.ListItem;

public record ListItemDTO(Long id,RadioDTO radio) {
	
	public ListItemDTO(ListItem entity) {
		this(entity.getId(),new RadioDTO(entity.getRadio()));
	}

}
