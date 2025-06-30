package com.jcsoftware.radios.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.radios.entities.dtos.NewRadioListDTO;
import com.jcsoftware.radios.entities.dtos.RadioListDTO;
import com.jcsoftware.radios.services.RadioListService;

@RestController
@RequestMapping(value = "/lists")
public class RadioListControllers {
	
	@Autowired
	private RadioListService service;
	
	@GetMapping(value="/{id}")
	public ResponseEntity<RadioListDTO> findById(@PathVariable Long id){
		RadioListDTO listDTO = service.findById(id);
		return ResponseEntity.ok().body(listDTO);
	}
	
	 @PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@PostMapping()
	public ResponseEntity<RadioListDTO> insert(@RequestBody NewRadioListDTO dto){
		 RadioListDTO radioListDTO = service.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(radioListDTO.id()).toUri();
			
			return ResponseEntity.created(uri).body(radioListDTO);
	}

}

// @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")