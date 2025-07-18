package com.jcsoftware.radios.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcsoftware.radios.entities.dtos.UserDTO;
import com.jcsoftware.radios.entities.dtos.UserUpdateDTO;
import com.jcsoftware.radios.entities.dtos.UserWithRolesDTO;
import com.jcsoftware.radios.services.UserService;

import jakarta.validation.Valid;

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
	
	@PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@PutMapping(value="/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto){
		
		UserDTO  user = service.update(id, dto);
		return ResponseEntity.ok().body(user);
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}

}
