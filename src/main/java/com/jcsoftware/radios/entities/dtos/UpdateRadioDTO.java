package com.jcsoftware.radios.entities.dtos;

import java.util.ArrayList;
import java.util.List;

public record UpdateRadioDTO(
		String name,
		String shortName,
		String streamUrl,
		String imageUrl,
		Boolean hsl,
		Boolean webRadio,
		Long cityId,
		List<CategoryDTO> categories
		) {
	
	public UpdateRadioDTO(
	        String name,
	        String shortName,
	        String streamUrl,
	        String imageUrl,
	        Boolean hsl,
	        Boolean webRadio,
	        Long cityId
	    ) {
	        this(name, shortName, streamUrl, imageUrl, hsl, webRadio, cityId, new ArrayList<>());
	    }
	
}
