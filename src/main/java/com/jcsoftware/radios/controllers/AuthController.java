package com.jcsoftware.radios.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.entities.dtos.UserDTO;
import com.jcsoftware.radios.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value="/me")
	public ResponseEntity<UserDTO> findById(){
		User user = userService.me();
		return ResponseEntity.ok().body(new UserDTO(user));
	}

}
