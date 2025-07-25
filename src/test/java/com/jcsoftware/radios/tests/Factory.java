package com.jcsoftware.radios.tests;

import com.jcsoftware.radios.entities.Category;
import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.ListItem;
import com.jcsoftware.radios.entities.Radio;
import com.jcsoftware.radios.entities.RadioList;
import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.ListItemDTO;
import com.jcsoftware.radios.entities.enums.State;

public class Factory {
	
	public static Category createCategory() {
		return new Category(1L,"Esportes");
	}
	
   
	public static CategoryDTO createCategoryDTO(Category entity) {
		  return new CategoryDTO(entity);
	}
	
	public static Radio createRadio() {
		City city = new City(1L,"Campos do Jordão",State.SP);
		Radio radio = new Radio(1L,"Rádio da Cidade","Rádio da Cidade","http://xxx.com","http://www.ccc.com",false,city);
		return radio;
	}
	
	public static User createUser() {
		User user = new User(1L,"Joyce","joyce@gmail.com","123456");
		return user;
	}
	
	public static RadioList createRadioList() {
		RadioList radioList = new RadioList(1L,"Lista da Joyce",createUser());
		return radioList;
	}
	
	public static ListItem createListItem() {
		ListItem listItem = new ListItem(1L,createRadioList(),createRadio());
		return listItem;
	}
	
	public static ListItemDTO createListItemDTO(ListItem entity) {
		return new ListItemDTO(entity);
	}
	
	
	
	

}
