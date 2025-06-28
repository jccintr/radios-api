package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.Category;

public record CategoryDTO(Long id,String name) {
	public CategoryDTO(Category entity) {
		this(
				entity.getId(),
				entity.getName()
				);
	}
}
