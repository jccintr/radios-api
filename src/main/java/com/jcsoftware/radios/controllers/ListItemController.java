package com.jcsoftware.radios.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.radios.entities.dtos.ListItemDTO;
import com.jcsoftware.radios.entities.dtos.NewListItemDTO;
import com.jcsoftware.radios.services.ListItemService;

@RestController
@RequestMapping(value = "/items")
public class ListItemController {
	
	@Autowired
	private ListItemService service;
	
	@PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@PostMapping()
	public ResponseEntity<ListItemDTO> insert(@RequestBody NewListItemDTO dto){
		ListItemDTO newListItem = service.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(newListItem.id()).toUri();
			
			return ResponseEntity.created(uri).body(newListItem);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_COMMON','ROLE_ADMIN')")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}

}
