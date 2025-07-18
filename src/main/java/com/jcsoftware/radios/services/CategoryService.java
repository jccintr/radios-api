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

import com.jcsoftware.radios.entities.Category;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.NewCategoryDTO;
import com.jcsoftware.radios.repositories.CategoryRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;

	@Transactional
	public CategoryDTO insert(NewCategoryDTO dto) {
		Category newCategory = new Category();
		BeanUtils.copyProperties(dto, newCategory);
		newCategory = repository.save(newCategory);
		return new CategoryDTO(newCategory);
	}
	
	public List<CategoryDTO> findAll() {
		List<Category> categories = repository.findAll(Sort.by("name"));
		return categories.stream().map(CategoryDTO::new).toList();
	}
	
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		//http://localhost:8080/categories?page=0
		Pageable customPageable = PageRequest.of(
	            pageable.getPageNumber(),
	            10,                     
	            Sort.by("name").ascending() 
	        );
		Page<Category> categories = repository.findAll(customPageable);
		return categories.map(CategoryDTO::new);
	}

	public void delete(Long id) {
		
        try {	
			
			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw (new ResourceNotFoundException("Category not found id: "+id));
			}
			
		} catch(DataIntegrityViolationException e) {
		    throw (new DatabaseIntegrityViolationException(id));
		}
		
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		
		try {
			Category category = repository.getReferenceById(id);
			copyDtoToEntity(dto,category);
			category = repository.save(category);
			return new CategoryDTO(category);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException("Category not found id: " + id));
		}
		
	}
	
	private void copyDtoToEntity(CategoryDTO dto, Category entity) {

		entity.setName(dto.name());
		
		
	}

	public CategoryDTO findById(Long id) {
		Optional<Category> categoryO = repository.findById(id);
		Category category = categoryO.orElseThrow(() -> new ResourceNotFoundException("Category not found id: " + id));
		
		return new CategoryDTO(category);
	}


}
