package com.jcsoftware.radios.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.entities.dtos.LoginRequest;
import com.jcsoftware.radios.entities.dtos.LoginResponse;
import com.jcsoftware.radios.entities.dtos.RegisterDTO;
import com.jcsoftware.radios.entities.dtos.UserDTO;
import com.jcsoftware.radios.services.AuthService;
import com.jcsoftware.radios.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuthService authService;
	
	@GetMapping(value="/me")
	public ResponseEntity<UserDTO> findById(){
		User user = userService.me();
		return ResponseEntity.ok().body(new UserDTO(user));
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<UserDTO> insert(@RequestBody @Valid RegisterDTO dto){
		UserDTO newUserDTO = userService.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(newUserDTO.id()).toUri();
			
			return ResponseEntity.created(uri).body(newUserDTO);
	}
	
	@PostMapping(value="/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
		LoginResponse response = authService.login(request);
		return ResponseEntity.ok().body(response);
	}

}
