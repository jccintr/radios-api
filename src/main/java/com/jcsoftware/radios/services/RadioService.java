package com.jcsoftware.radios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.radios.entities.Category;
import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.Radio;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.NewRadioDTO;
import com.jcsoftware.radios.entities.dtos.RadioDTO;
import com.jcsoftware.radios.entities.dtos.UpdateRadioDTO;
import com.jcsoftware.radios.repositories.CategoryRepository;
import com.jcsoftware.radios.repositories.CityRepository;
import com.jcsoftware.radios.repositories.RadioRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RadioService {
	
	 private final RadioRepository repository;
	 private final CityRepository cityRepository;
	 private final CategoryRepository categoryRepository;
	 
	 public RadioService(RadioRepository repository,CityRepository cityRepository,CategoryRepository categoryRepository) {
        this.repository = repository;
        this.cityRepository = cityRepository;
        this.categoryRepository = categoryRepository;
    }

	@Transactional
	public RadioDTO insert(NewRadioDTO dto) {
		Radio newRadio = new Radio();
		copyDtoToEntity(dto,newRadio);
		newRadio = repository.save(newRadio);
		return new RadioDTO(newRadio);
	}

	public List<RadioDTO> findAll() {
		List<Radio> radios = repository.findAll(Sort.by("name"));
		return radios.stream().map(RadioDTO::new).toList();
	}
	
	public Page<RadioDTO> findAllPaged(Pageable pageable) {
		Pageable customPageable = PageRequest.of(
	            pageable.getPageNumber(),
	            10,                     
	            Sort.by("name").ascending() 
	        );
		Page<Radio> radios = repository.findAll(customPageable);
		return radios.map(RadioDTO::new);
	}
	
	

    public void delete(Long id) {
		
        try {	
			
			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw (new ResourceNotFoundException("Radio not found id: "+id));
			}
			
		} catch(DataIntegrityViolationException e) {
		    throw (new DatabaseIntegrityViolationException(id));
		}
		
	}

	@Transactional
	public RadioDTO update(Long id, UpdateRadioDTO dto) {
		
		try {
			Radio radio = repository.getReferenceById(id);
			copyDtoToEntity(dto,radio);
			radio = repository.save(radio);
			return new RadioDTO(radio);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException("Radio not found id: "+ id));
		}
		
	}
	
	private void copyDtoToEntity(NewRadioDTO dto, Radio entity) {

		entity.setName(dto.name());
		entity.setShortName(dto.shortName());
		entity.setImageUrl(dto.imageUrl());
        entity.setStreamUrl(dto.streamUrl());
        entity.setHsl(dto.hsl());
        City city = cityRepository.getReferenceById(dto.cityId());
        entity.setCity(city);
        entity.getCategories().clear();
		for(CategoryDTO catDTO: dto.categories()) {
			Category cat = categoryRepository.getReferenceById(catDTO.id());
			cat.setId(catDTO.id());
			entity.getCategories().add(cat);
		}
        
		
	}
	
	private void copyDtoToEntity(UpdateRadioDTO dto, Radio entity) {

		entity.setName(dto.name());
		entity.setShortName(dto.shortName());
		entity.setImageUrl(dto.imageUrl());
        entity.setStreamUrl(dto.streamUrl());
        entity.setHsl(dto.hsl());
        City city = cityRepository.getReferenceById(dto.cityId());
        entity.setCity(city);
        entity.getCategories().clear();
		for(CategoryDTO catDTO: dto.categories()) {
			Category cat = categoryRepository.getReferenceById(catDTO.id());
			cat.setId(catDTO.id());
			entity.getCategories().add(cat);
		}
        
		
	}

	public RadioDTO findById(Long id) {
		Optional<Radio> radioO = repository.findById(id);
		Radio radio = radioO.orElseThrow(() -> new ResourceNotFoundException("Radio not found id: "+ id));
		
		return new RadioDTO(radio);
	}

	
    

}
