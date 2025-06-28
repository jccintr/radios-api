package com.jcsoftware.radios.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.radios.entities.dtos.NewRadioDTO;
import com.jcsoftware.radios.entities.dtos.RadioDTO;
import com.jcsoftware.radios.entities.dtos.UpdateRadioDTO;
import com.jcsoftware.radios.services.RadioService;

@RestController
@RequestMapping(value = "/radios")
public class RadioController {
	
	@Autowired
	private RadioService service;
	
	@PostMapping()
	public ResponseEntity<RadioDTO> insert(@RequestBody NewRadioDTO dto){
		RadioDTO radioDTO = service.insert(dto);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(radioDTO.id()).toUri();
			
			return ResponseEntity.created(uri).body(radioDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<RadioDTO>> findAll(){
		List<RadioDTO> radios = service.findAll();
        return ResponseEntity.ok().body(radios);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<RadioDTO> findById(@PathVariable Long id){
		RadioDTO radioDTO = service.findById(id);
		return ResponseEntity.ok().body(radioDTO);
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<RadioDTO> update(@PathVariable Long id, @RequestBody UpdateRadioDTO dto){
		
		RadioDTO  radio = service.update(id, dto);
		return ResponseEntity.ok().body(radio);
		
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}

}
