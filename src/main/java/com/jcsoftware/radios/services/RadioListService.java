package com.jcsoftware.radios.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcsoftware.radios.entities.RadioList;
import com.jcsoftware.radios.entities.dtos.RadioListDTO;
import com.jcsoftware.radios.repositories.RadioListRepository;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

@Service
public class RadioListService {
	
	@Autowired
	private RadioListRepository repository;

	public RadioListDTO findById(Long id) {
		Optional<RadioList> radioListO = repository.findById(id);
		RadioList radioList = radioListO.orElseThrow(() -> new ResourceNotFoundException());
		return new RadioListDTO(radioList);
	}

}
