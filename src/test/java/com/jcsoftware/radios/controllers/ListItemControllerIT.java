package com.jcsoftware.radios.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

/*
User logged
     RadioListExists
        UserIsOwner
           RadioExists
               RadioIsNotInList
                   ValidData -> ok
                  
               RadioAlreadyInList -> ok
           RadioDoesNotExists -> ok
        UserIsNotOwner -> ok
     RadioListDoesNotExists -> ok
User Not Logged -> ok
 */

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ListItemControllerIT {
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {}) 
	public void insert_ShouldPersistListItem_WhenAuthenticated_RadioListExist_UserIsOwner_RadioExist_RadioIsNotInList_AndValidData() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {}) 
    public void insert_ShouldReturnUnproccessableEntity_WhenInvalidRadioListId() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {}) 
    public void insert_ShouldReturnUnproccessableEntity_WhenInvalidRadioId() throws Exception {
		
   	}

	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {}) 
	public void insert_ShouldReturnNotFound_WhenAuthenticated_RadioListDoesNotExist() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {}) 
	public void insert_ShouldReturnNotFound_WhenAuthenticated_RadioListExist_UserIsOwner_AndRadioDoesNotExist() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {}) 
	public void insert_ShouldReturnConflict_WhenAuthenticated_RadioListExist_UserIsOwner_RadioExist_AndRadioAlreadyInList() throws Exception {
		
	}
	
	@Test
	@WithMockUser(username = "jccintr@gmail.com", roles = {}) 
	public void insert_ShouldReturnForbidden_WhenAuthenticated_RadioListExist_AndUserIsNotOwner() throws Exception {
		
	}
	
	@Test
	public void insert_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
	}
	
	
	
	@Test
	public void delete_ShouldReturnNoContent_WhenAuthenticated_AndListItemExist() throws Exception {
		
	}
	
	@Test
	public void delete_ShouldReturnForbidden_WhenAuthenticated_ListItemExist_AndUserIsNotOwner() throws Exception {
		
	}
	
	@Test
	public void delete_ShouldReturnNotFound_WhenAuthenticated_AndListItemDoesNotExist() throws Exception {
		
	}
	
	@Test
	public void delete_ShouldReturnUnauthorized_WhenNoLoggedUser() throws Exception {
		
	}

}
