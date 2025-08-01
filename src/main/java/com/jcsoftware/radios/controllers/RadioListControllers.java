package com.jcsoftware.radios.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.jcsoftware.radios.entities.dtos.NewRadioListDTO;
import com.jcsoftware.radios.entities.dtos.RadioListDTO;
import com.jcsoftware.radios.services.RadioListService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/lists")
public class RadioListControllers {
	
	@Autowired
	private RadioListService service;
	
	@PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@GetMapping(value="/{id}")
	public ResponseEntity<RadioListDTO> findById(@PathVariable Long id){
		RadioListDTO listDTO = service.findById(id);
		return ResponseEntity.ok().body(listDTO);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@GetMapping(value="/owner")
	public ResponseEntity<List<RadioListDTO>> findAllByOwner(){
		List<RadioListDTO> lists = service.findAllByOwner();
		return ResponseEntity.ok().body(lists);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@PostMapping()
	public ResponseEntity<RadioListDTO> insert(@RequestBody @Valid NewRadioListDTO dto){
		 RadioListDTO radioListDTO = service.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(radioListDTO.id()).toUri();
			
			return ResponseEntity.created(uri).body(radioListDTO);
	}
	 
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
	 @GetMapping
	 public ResponseEntity<Page<RadioListDTO>> findAll(Pageable pageable){
		Page<RadioListDTO> lists = service.findAll(pageable);
        return ResponseEntity.ok().body(lists);
   	}
	 
	 @PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	 @DeleteMapping(value="/{id}")
	 public ResponseEntity<Void> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	 
	@PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@PutMapping(value="/{id}")
	public ResponseEntity<RadioListDTO> update(@PathVariable Long id, @RequestBody @Valid NewRadioListDTO dto){
			
		RadioListDTO  radioList = service.update(id, dto);
		return ResponseEntity.ok().body(radioList);
			
	}

}

