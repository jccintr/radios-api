package com.jcsoftware.radios.entities.dtos;

import java.util.List;

import com.jcsoftware.radios.entities.Radio;

public record RadioDTO(Long id,String name,String shortName,String streamUrl,String imageUrl,Boolean hsl,Boolean webRadio, CityDTO city,List<CategoryDTO> categories) {
	
	public RadioDTO(Radio entity) {
		this(
				entity.getId(),
				entity.getName(),
				entity.getShortName(),
				entity.getStreamUrl(),
				entity.getImageUrl(),
				entity.getHsl(),
				entity.getWebRadio(),
				new CityDTO(entity.getCity()),
				entity.getCategories().stream().map(CategoryDTO::new).toList()
				);
	}
}
