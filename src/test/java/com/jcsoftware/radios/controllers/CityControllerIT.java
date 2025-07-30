package com.jcsoftware.radios.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.jcsoftware.radios.entities.dtos.CityDTO;
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
		
		NewCityDTO newCity = new NewCityDTO("Br",State.MG);
		String jsonBody = objectMapper.writeValueAsString(newCity);
		ResultActions result =
				mockMvc.perform(post("/cities")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.status").value(422));
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("O campo deve ter pelo menos 3 caracteres"));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void insert_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndNonExistentState() throws Exception {
		
		 String jsonBody = """
			        {
			          "name": "Ubatuba",
			          "state": "SS"
			        }
			    """;
		 
		 ResultActions result =
					mockMvc.perform(post("/cities")
							.content(jsonBody)
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.APPLICATION_JSON));
			
			result.andExpect(status().isUnprocessableEntity());
			result.andExpect(jsonPath("$.status").value(422));
			result.andExpect(jsonPath("$.errors[0].fieldName").value("state"));
			result.andExpect(jsonPath("$.errors[0].message").value("Estado inexistente"));
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void insert_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
		NewCityDTO newCity = new NewCityDTO("Ubatuba",State.MG);
		String jsonBody = objectMapper.writeValueAsString(newCity);
		ResultActions result =
				mockMvc.perform(post("/cities")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void insert_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
		NewCityDTO newCity = new NewCityDTO("Ubatuba",State.MG);
		String jsonBody = objectMapper.writeValueAsString(newCity);
		ResultActions result =
				mockMvc.perform(post("/cities")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	public void findById_ShouldReturnCity_WhenExistingId()  throws Exception {
		
		Long existingId = 1L;
		ResultActions result =
				mockMvc.perform(get("/cities/{id}",existingId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").isNotEmpty());
		
	}
	
	@Test
	public void findById_ShouldReturnNotFound_WhenNonExistentId()  throws Exception {
		
		Long nonExistentId = 99L;
		ResultActions result =
				mockMvc.perform(get("/cities/{id}",nonExistentId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
		result.andExpect(jsonPath("$.message").value("City not found id: "+ nonExistentId));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldUpdateCity_WhenAdminLoggedAndValidData() throws Exception {
		
		Long existingId = 1L;
		CityDTO updatedCity = new CityDTO(existingId,"City Updated",State.MG);
		String jsonBody = objectMapper.writeValueAsString(updatedCity);
		ResultActions result = mockMvc.perform( put("/cities/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.name").value("City Updated"));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidCityName() throws Exception {
		
		Long existingId = 1L;
		CityDTO updatedCity = new CityDTO(existingId,"Ci",State.MG);
		String jsonBody = objectMapper.writeValueAsString(updatedCity);
		ResultActions result = mockMvc.perform( put("/cities/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.status").value(422));
		result.andExpect(jsonPath("$.message").value("Invalid parameters"));
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("O campo deve ter pelo menos 3 caracteres"));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidState() throws Exception {
		
		Long existingId = 1L;
		 String jsonBody = """
			        {
			          "name": "City Updated",
			          "state": "SS"
			        }
			    """;
		ResultActions result = mockMvc.perform( put("/cities/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.status").value(422));
		result.andExpect(jsonPath("$.message").value("Invalid parameters"));
		result.andExpect(jsonPath("$.errors[0].fieldName").value("state"));
		result.andExpect(jsonPath("$.errors[0].message").value("Estado inexistente"));
		
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void update_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
		Long existingId = 1L;
		CityDTO updatedCity = new CityDTO(existingId,"City Updated",State.MG);
		String jsonBody = objectMapper.writeValueAsString(updatedCity);
		ResultActions result = mockMvc.perform( put("/cities/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isForbidden());
	
	}
	
	@Test
	public void update_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
		Long existingId = 1L;
		CityDTO updatedCity = new CityDTO(existingId,"City Updated",State.MG);
		String jsonBody = objectMapper.writeValueAsString(updatedCity);
		ResultActions result = mockMvc.perform( put("/cities/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnNotFound_WhenNonExistentIdAndAdminLogged()  throws Exception {
		
		Long nonExistentId = 99L;
		CityDTO updatedCity = new CityDTO(nonExistentId,"City Updated",State.MG);
		String jsonBody = objectMapper.writeValueAsString(updatedCity);
		ResultActions result = mockMvc.perform( put("/cities/{id}",nonExistentId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
		result.andExpect(jsonPath("$.message").value("City not found id: "+ nonExistentId));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void delete_ShouldReturnNoContent_WhenAdminLoggedAndIsNotReferenced() throws Exception {
		
		Long nonReferencedId = 1L;
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}",nonReferencedId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void delete_ShouldReturnNotFound_WhenAdminLoggedAndCityDoesNotExist() throws Exception {
		
		Long nonExistentId = 99L;
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}",nonExistentId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
		result.andExpect(jsonPath("$.message").value("City not found id: "+ nonExistentId));
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void delete_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
		Long nonReferencedId = 18L;
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}",nonReferencedId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void delete_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
		Long nonReferencedId = 18L;
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}",nonReferencedId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnauthorized());
		
	}

}
