package com.jcsoftware.radios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jcsoftware.radios.entities.Category;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.NewCategoryDTO;
import com.jcsoftware.radios.repositories.CategoryRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
	
	@InjectMocks
	private CategoryService service;
	@Mock
	private CategoryRepository repository;
	
	@BeforeEach
	void setup() throws Exception {
	
	}
	
	@Test
	public void insert_ShouldPersistCategoryAndReturnCategoryDTO() {
		
		NewCategoryDTO newCategoryDTO = new NewCategoryDTO("Esportes");
		
		Category savedCategory = new Category(1L,newCategoryDTO.name());
		
		
		when(repository.save(any(Category.class))).thenReturn(savedCategory);
		
		CategoryDTO result = service.insert(newCategoryDTO);
		
		assertEquals(newCategoryDTO.name(), result.name());
        verify(repository, times(1)).save(any(Category.class));
		
	}
	
	@Test
	public void findById_ShouldReturnCategoryDTO_WhenCategoryExists() {
		
		Long existingId = 1L;
		Category mockCategory = new Category(1L,"Gospel");
		
		when(repository.findById(existingId)).thenReturn(Optional.of(mockCategory));
		
	    CategoryDTO result = service.findById(existingId);
	    
		Assertions.assertNotNull(result);
		 assertEquals(mockCategory.getName(), result.name());
		 
	}
	
	
	@Test
	public void findById_ShouldThrowResourceNotFoundExceptionO_WhenCategoryDoesNotExists() {
		
		 Long nonExistentId = 99L;
		 
		 when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
		 
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.findById(nonExistentId);
		});
		verify(repository).findById(nonExistentId);
		
	}
	
	
	@Test
	public void delete_ShouldSucceed_WhenCategoryExistsAndIsNotReferenced() {
		
		Long validId = 5L;
		
        when(repository.existsById(validId)).thenReturn(true);
		 
		 Assertions.assertDoesNotThrow(()->service.delete(validId));
		 
		 verify(repository).deleteById(validId);
	}
	
	 @Test
	 void delete_ShouldThrowResourceNotFoundException_WhenCategoryDoesNotExist() {
		
         Long nonExistentId = 99L;
		 
		 when(repository.existsById(nonExistentId)).thenReturn(false);
		 
		 assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
	     verify(repository, never()).delete(any());
	     
	}
	
	 @Test
	 void delete_ShouldThrow_DatabaseIntegrityViolationException_WhenCategoryIsReferenced() {
		 
		 Long referencedId = 99L;
		 
         when(repository.existsById(referencedId)).thenReturn(true);
         
         doThrow(DatabaseIntegrityViolationException.class).when(repository).deleteById(referencedId);
         
	     assertThrows(DatabaseIntegrityViolationException.class, () -> service.delete(referencedId));
	     verify(repository, never()).delete(any());
	     
	 }
	
	
	 @Test
	 void update_ShouldSucceed_WhenCategoryExist() {
		 
		 Long validId = 5L;
		 Category mockCategory = new Category(validId,"Gospel");
		 CategoryDTO categoryDTO = new CategoryDTO(mockCategory);
		 Category updatedCategory = new Category(validId,"Gospel Updated");
		 
		 when(repository.getReferenceById(validId)).thenReturn(mockCategory);
		 when(repository.save(any(Category.class))).thenReturn(updatedCategory);
		 
		 CategoryDTO result = service.update(validId, categoryDTO);
		 
		 Assertions.assertNotNull(result);
		 assertEquals(validId, result.id());
		 assertEquals(updatedCategory.getName(), result.name());
	     verify(repository).getReferenceById(validId);
		
	}
	
	@Test
	 void update_ShouldThrowResourceNotFoundException_WhenCategoryDoesNotExist() {
		
		 Long nonExistentId = 99L;
		 Category mockCategory = new Category(nonExistentId,"Gospel");
		 CategoryDTO categoryDTO = new CategoryDTO(mockCategory);
		 
		 when(repository.getReferenceById(nonExistentId)).thenThrow(ResourceNotFoundException.class);
	       
		 assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, categoryDTO));
	     verify(repository, never()).save(any());
	     
	}
	
	
	@Test
    void findAllPaged_ShouldReturnSortedPagedOfCategoryDTOs() {
		
		Category mockCategory = new Category(1L,"Gospel");
		
		 List<Category> content = List.of(mockCategory);
	     Pageable inputPageable = PageRequest.of(0, 5);
	     Page<Category> page = new PageImpl<>(content);
	     Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("name").ascending());
	     
	     when(repository.findAll(expectedPageable)).thenReturn(page);
       
        Page<CategoryDTO> result = service.findAllPaged(inputPageable);
        
        assertEquals(1, result.getTotalElements());
	    assertEquals(mockCategory.getName(), result.getContent().get(0).name());
	    verify(repository).findAll(expectedPageable);
        
    }
	
	@Test
    void findAll_ShouldReturnSortedListOfCategoryDTOs() {
		
		 Category mockCategory1 = new Category(1L,"Gospel");
		 Category mockCategory2 = new Category(1L,"MPB");
        
		 Sort sort = Sort.by("name");
		  List<Category> categoryList = List.of(mockCategory1, mockCategory2);
		  when(repository.findAll(sort)).thenReturn(categoryList);
		  
		  List<CategoryDTO> result = service.findAll();
		  
		  assertEquals(categoryList.size(), result.size());
	      assertEquals(categoryList.get(0).getName(), result.get(0).name());
	      assertEquals(categoryList.get(1).getName(), result.get(1).name());
	      verify(repository).findAll(sort);
    }
	
	

}
