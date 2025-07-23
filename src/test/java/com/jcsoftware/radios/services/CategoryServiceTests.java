package com.jcsoftware.radios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jcsoftware.radios.entities.Category;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.repositories.CategoryRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;
import com.jcsoftware.radios.tests.Factory;


@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
	
	@InjectMocks
	private CategoryService service;
	private Category category;
	private CategoryDTO categoryDTO;
	private List<Category> categoryList;
	private PageImpl<Category> categoryPage;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Pageable pageable;
	
	@Mock
	private CategoryRepository repository;
	
	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 10L;
		pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
		category = Factory.createCategory();
		categoryDTO = Factory.createCategoryDTO(category);
		categoryPage = new PageImpl<>(List.of(category), pageable, 1);
		categoryList = List.of(
	            new Category(1L, "Esportes"),
	            new Category(2L, "Rock")
	    );
		
		when(repository.findAll(any(Sort.class))).thenReturn(categoryList);
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(categoryPage);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(category));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(category);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(category);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void findById_ShouldReturnCategoryDTOWhenIdExists() {
	    CategoryDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findById_ShouldThrowResourceNotFoundExceptionOWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void findById_ShouldReturnCategotyDTOWhenIdExists() {
		
		CategoryDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()->{
			service.delete(existingId);
		});
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void delete_ShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void delete_ShouldThrowIntegrityViolationExceptionWhenDependentid() {
		Assertions.assertThrows(DatabaseIntegrityViolationException.class,()->{
			service.delete(dependentId);
		});
	}
	
	@Test
	public void update_ShouldReturnCategoryDTOWhenIdExists() {
		CategoryDTO result = service.update(existingId, categoryDTO);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void update_ShouldThrowResourceNotFoundExceptionOWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.update(nonExistingId, categoryDTO);
		});
	}
	
	@Test
    void findAllPaged_ShouldReturnPagedCategoryDTOs() {
       
        Page<CategoryDTO> result = service.findAllPaged(pageable);
       
        assertNotNull(result, "A página retornada não deve ser nula");
        assertEquals(1, result.getTotalElements(), "O total de elementos deve ser 1");
        assertEquals(1, result.getContent().size(), "A página deve conter 1 elemento");
        assertEquals("Esportes", result.getContent().get(0).name(), "O nome da categoria deve ser 'Esportes'");
        
    }
	
	@Test
    void findAll_ShouldReturnSortedCategoryDTOs() {
        
        List<CategoryDTO> result = service.findAll();
        
        assertNotNull(result, "A lista retornada não deve ser nula");
        assertEquals(2, result.size(), "A lista deve conter 2 elementos");
        assertEquals("Esportes", result.get(0).name(), "A primeira categoria deve ser 'Esportes'");
        assertEquals("Rock", result.get(1).name(), "A segunda categoria deve ser 'Rock'");

    }
	
	

}
