package com.jcsoftware.radios.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.radios.entities.dtos.CityDTO;
import com.jcsoftware.radios.entities.dtos.NewCityDTO;
import com.jcsoftware.radios.services.CityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/cities")
public class CityController {
	
	@Autowired
	private CityService service;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping()
	public ResponseEntity<CityDTO> insert(@RequestBody @Valid NewCityDTO dto){
		CityDTO cityDTO = service.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(cityDTO.id()).toUri();
			
			return ResponseEntity.created(uri).body(cityDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<CityDTO>> findAll(){
		List<CityDTO> cities = service.findAll();
        return ResponseEntity.ok().body(cities);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<CityDTO> findById(@PathVariable Long id){
		CityDTO cityDTO = service.findById(id);
		return ResponseEntity.ok().body(cityDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value="/{id}")
	public ResponseEntity<CityDTO> update(@PathVariable Long id, @RequestBody @Valid CityDTO dto){
		
		CityDTO  city = service.update(id, dto);
		return ResponseEntity.ok().body(city);
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}



}
