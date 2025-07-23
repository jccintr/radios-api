package com.jcsoftware.radios.tests;

import com.jcsoftware.radios.entities.Category;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;

public class Factory {
	
	public static Category createCategory() {
		
		return new Category(1L,"Esportes");
		
	}
	
   
	public static CategoryDTO createCategoryDTO(Category entity) {
		  return new CategoryDTO(entity);
	}
	
	
	
	

}
