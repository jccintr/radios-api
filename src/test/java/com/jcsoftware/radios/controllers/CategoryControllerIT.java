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
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.NewCategoryDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void findAll_ShouldReturn_OrderedListOfCategory() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/categories/all").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.length()").value(20));
		result.andExpect(jsonPath("$.[0].name").value("Anos 80"));
		result.andExpect(jsonPath("$.[2].name").value("Carnaval"));
	}
	
	@Test
	public void findAll_ShouldReturn_OrderedPageOfCategory() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/categories?page=0").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(20));
		result.andExpect(jsonPath("$.content[0].name").value("Anos 80"));
		result.andExpect(jsonPath("$.content[2].name").value("Carnaval"));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void insert_ShouldPersistCategory_WhenAdminLoggedAndValidData() throws Exception {
		
		NewCategoryDTO newCategory = new NewCategoryDTO("New Category");
		String jsonBody = objectMapper.writeValueAsString(newCategory);
		
		ResultActions result =
				mockMvc.perform(post("/categories")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.name").value(newCategory.name()));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void insert_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidCategoryName() throws Exception {
		
		NewCategoryDTO newCategory = new NewCategoryDTO("Ne");
		String jsonBody = objectMapper.writeValueAsString(newCategory);
		
		ResultActions result =
				mockMvc.perform(post("/categories")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void insert_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
		NewCategoryDTO newCategory = new NewCategoryDTO("New Category");
		String jsonBody = objectMapper.writeValueAsString(newCategory);
		
		ResultActions result =
				mockMvc.perform(post("/categories")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void insert_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
		NewCategoryDTO newCategory = new NewCategoryDTO("New Category");
		String jsonBody = objectMapper.writeValueAsString(newCategory);
		
		ResultActions result =
				mockMvc.perform(post("/categories")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	public void findById_ShouldReturnCategory_WhenExistingId()  throws Exception {
		
		Long existingId = 1L;
		ResultActions result =
				mockMvc.perform(get("/categories/{id}",existingId)
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
				mockMvc.perform(get("/categories/{id}",nonExistentId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
		result.andExpect(jsonPath("$.message").value("Category not found id: "+ nonExistentId));
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldUpdateCategory_WhenAdminLoggedAndValidData() throws Exception {
		
		Long existingId = 1L;
		CategoryDTO categoryDTO = new CategoryDTO(1L,"Category updated");
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		ResultActions result = mockMvc.perform( put("/categories/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.name").value(categoryDTO.name()));
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnUnproccessableEntity_WhenAdminLoggedAndInvalidCategoryName() throws Exception {
		Long existingId = 1L;
		CategoryDTO categoryDTO = new CategoryDTO(1L,"Ca");
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		ResultActions result = mockMvc.perform( put("/categories/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("O campo deve ter pelo menos 3 caracteres"));
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void update_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		Long existingId = 1L;
		CategoryDTO categoryDTO = new CategoryDTO(1L,"Category updated");
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		ResultActions result = mockMvc.perform( put("/categories/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void update_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		Long existingId = 1L;
		CategoryDTO categoryDTO = new CategoryDTO(1L,"Category updated");
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		ResultActions result = mockMvc.perform( put("/categories/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void update_ShouldReturnNotFound_WhenNonExistentIdAndAdminLogged()  throws Exception {
		
		Long nonExistentId = 99L;
		CategoryDTO categoryDTO = new CategoryDTO(1L,"Category updated");
		String jsonBody = objectMapper.writeValueAsString(categoryDTO);
		ResultActions result = mockMvc.perform( put("/categories/{id}",nonExistentId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void delete_ShouldReturnNoContent_WhenAdminLoggedAndIsNotReferenced() throws Exception {
		
		Long nonReferencedId = 18L;
		ResultActions result =
				mockMvc.perform(delete("/categories/{id}",nonReferencedId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {"ADMIN"}) 
	public void delete_ShouldReturnNotFound_WhenAdminLoggedAndCategoryDoesNotExist() throws Exception {
		
		Long nonExistentId = 99L;
		ResultActions result =
				mockMvc.perform(delete("/categories/{id}",nonExistentId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.error").value("Resource not found"));
		result.andExpect(jsonPath("$.message").value("Category not found id: "+ nonExistentId));
		
	}
	
	@Test
	@WithMockUser(username = "alex@gmail.com", roles = {"COMMON"}) 
	public void delete_ShouldReturnForbidden_WhenNotAdminLogged() throws Exception {
		
		Long nonReferencedId = 18L;
		ResultActions result =
				mockMvc.perform(delete("/categories/{id}",nonReferencedId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void delete_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
		Long nonReferencedId = 18L;
		ResultActions result =
				mockMvc.perform(delete("/categories/{id}",nonReferencedId)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnauthorized());
		
	}
	
	
	
}
