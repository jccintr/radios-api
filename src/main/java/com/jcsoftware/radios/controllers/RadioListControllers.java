package com.jcsoftware.radios.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
