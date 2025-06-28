package com.jcsoftware.radios.entities.dtos;

import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.enums.State;

public record CityDTO(Long id, String name, State state) {
	public CityDTO(City entity) {
		this(
				entity.getId(),
				entity.getName(),
				entity.getState());
	}
}
