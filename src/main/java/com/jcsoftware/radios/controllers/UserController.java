package com.jcsoftware.radios.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcsoftware.radios.entities.dtos.UserWithRolesDTO;
import com.jcsoftware.radios.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	
	
	private final UserService service;
	
	public UserController(UserService service) {
		this.service = service;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<List<UserWithRolesDTO>> findAll(){
		List<UserWithRolesDTO> users = service.findAll();
        return ResponseEntity.ok().body(users);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<UserWithRolesDTO> findById(@PathVariable Long id){
		UserWithRolesDTO userDTO = service.findById(id);
		return ResponseEntity.ok().body(userDTO);
	}

}
