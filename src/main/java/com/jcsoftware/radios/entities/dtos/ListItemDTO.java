package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.ListItem;

public record ListItemDTO(RadioDTO radio) {
	
	public ListItemDTO(ListItem entity) {
		this(new RadioDTO(entity.getRadio()));
	}

}
