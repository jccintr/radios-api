package com.jcsoftware.radios.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcsoftware.radios.entities.dtos.NewCityDTO;
import com.jcsoftware.radios.entities.enums.State;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CityControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void findAll_ShouldReturn_OrderedListOfCity() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/cities/all").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.length()").value(15));
		result.andExpect(jsonPath("$.[0].name").value("Belo Horizonte"));
		result.andExpect(jsonPath("$.[2].name").value("Campos do Jordão"));
		
	}
	
	@Test
	public void findAll_ShouldReturn_OrderedPageOfCity() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/cities?page=0").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(15));
		result.andExpect(jsonPath("$.content[0].name").value("Belo Horizonte"));
		result.andExpect(jsonPath("$.content[2].name").value("Campos do Jordão"));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void insert_ShouldPersistCity_WhenAdminLoggedAndValidData() throws Exception {
		
		NewCityDTO newCity = new NewCityDTO("Brasópolis",State.MG);
		String jsonBody = objectMapper.writeValueAsString(newCity);
		ResultActions result =
				mockMvc.perform(post("/cities")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.name").value(newCity.name()));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void insert_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidCityName() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void insert_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidState() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void insert_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
	}
	
	@Test
	public void insert_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
	}
	
	@Test
	public void findById_ShouldReturnCity_WhenExistingId()  throws Exception {
		
	}
	
	@Test
	public void findById_ShouldReturnNotFound_WhenNonExistentId()  throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldUpdateCity_WhenAdminLoggedAndValidData() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidCityName() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidState() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void update_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
	}
	
	@Test
	public void update_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnNotFound_WhenNonExistentIdAndAdminLogged()  throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void delete_ShouldReturnNoContent_WhenAdminLoggedAndIsNotReferenced() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void delete_ShouldReturnNotFound_WhenAdminLoggedAndCityDoesNotExist() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void delete_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
	}
	
	@Test
	public void delete_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
	}

}
