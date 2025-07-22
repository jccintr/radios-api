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

import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.NewCategoryDTO;
import com.jcsoftware.radios.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping()
	public ResponseEntity<CategoryDTO> insert(@RequestBody @Valid NewCategoryDTO dto){
		CategoryDTO categoryDTO = service.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(categoryDTO.id()).toUri();
			
			return ResponseEntity.created(uri).body(categoryDTO);
	}
	
	
	@GetMapping(value="/all")
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> categories = service.findAll();
        return ResponseEntity.ok().body(categories);
	}
	
	
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAllPaged(Pageable pageable){
		Page<CategoryDTO> categories = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(categories);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO categoryDTO = service.findById(id);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody @Valid CategoryDTO dto){
		
		CategoryDTO  category = service.update(id, dto);
		return ResponseEntity.ok().body(category);
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}


}
