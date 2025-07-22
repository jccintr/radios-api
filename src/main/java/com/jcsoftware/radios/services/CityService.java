package com.jcsoftware.radios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.dtos.CityDTO;
import com.jcsoftware.radios.entities.dtos.NewCityDTO;
import com.jcsoftware.radios.repositories.CityRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;
	
	@Transactional
	public CityDTO insert(NewCityDTO dto) {
		City newCity = new City();
		BeanUtils.copyProperties(dto, newCity);
		newCity = repository.save(newCity);
		return new CityDTO(newCity);
	}
	
	public List<CityDTO> findAll() {
		List<City> cities = repository.findAll(Sort.by("name"));
		return cities.stream().map(CityDTO::new).toList();
	}

	
	public void delete(Long id) {
		
        try {	
			
			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw (new ResourceNotFoundException("City not found id: "+id));
			}
			
		} catch(DataIntegrityViolationException e) {
		    throw (new DatabaseIntegrityViolationException(id));
		}
		
	}

	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		
		try {
			City city = repository.getReferenceById(id);
			copyDtoToEntity(dto,city);
			city = repository.save(city);
			return new CityDTO(city);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException("City not found id: "+id));
		}
		
	}
	
	private void copyDtoToEntity(CityDTO dto, City entity) {

		entity.setName(dto.name());
		entity.setState(dto.state());
		
		
	}

	public CityDTO findById(Long id) {
		Optional<City> cityO = repository.findById(id);
		City city = cityO.orElseThrow(() -> new ResourceNotFoundException("City not found id: " + id));
		
		return new CityDTO(city);
	}

	public Page<CityDTO> findAllPaged(Pageable pageable) {
		
		Pageable customPageable = PageRequest.of(
	            pageable.getPageNumber(),
	            10,                     
	            Sort.by("name").ascending() 
	        );
		Page<City> cities = repository.findAll(customPageable);
		return cities.map(CityDTO::new);
		
	}

}
