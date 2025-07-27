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

import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.dtos.CityDTO;
import com.jcsoftware.radios.entities.dtos.NewCityDTO;
import com.jcsoftware.radios.entities.enums.State;
import com.jcsoftware.radios.repositories.CityRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CityServiceTests {
	
	@InjectMocks
	private CityService service;
	
	@Mock
	private CityRepository repository;
	
	@BeforeEach
	void setup() throws Exception {
		
	}
	
	@Test
	public void insert_ShouldPersistCityAndReturnCityyDTO() {
		
		NewCityDTO newCityDTO = new NewCityDTO("São Paulo",State.SP);
		City savedCity = new City(1L,newCityDTO.name(),newCityDTO.state());
		
        when(repository.save(any(City.class))).thenReturn(savedCity);
		
		CityDTO result = service.insert(newCityDTO);
		
		assertEquals(newCityDTO.name(), result.name());
		assertEquals(newCityDTO.state(), result.state());
        verify(repository, times(1)).save(any(City.class));
	}
	
	@Test
	public void findById_ShouldReturnCityDTO_WhenCityExists() {
		
		Long existingId = 1L;
		City mockedCity = new City(existingId,"Caçapava",State.SP);
		
		when(repository.findById(existingId)).thenReturn(Optional.of(mockedCity));
		CityDTO result = service.findById(existingId);
		    
		Assertions.assertNotNull(result);
	    assertEquals(mockedCity.getName(), result.name());
	    assertEquals(mockedCity.getState(), result.state());
	    verify(repository).findById(existingId);
		
	}
	
	@Test
	public void findById_ShouldThrowResourceNotFoundExceptionO_WhenCityDoesNotExists() {
		
		Long nonExistentId = 99L;
		 
		 when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
		 
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.findById(nonExistentId);
		});
		verify(repository).findById(nonExistentId);
		
	}
	
	@Test
	public void delete_ShouldSucceed_WhenCityyExistsAndIsNotReferenced() {
        
		Long validId = 5L;
		
        when(repository.existsById(validId)).thenReturn(true);
		 
		 Assertions.assertDoesNotThrow(()->service.delete(validId));
		 
		 verify(repository).deleteById(validId);
		
	}
	
	 @Test
	 void delete_ShouldThrowResourceNotFoundException_WhenCityyDoesNotExist() {
		 
         Long nonExistentId = 99L;
		 
		 when(repository.existsById(nonExistentId)).thenReturn(false);
		 
		 assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
	     verify(repository, never()).delete(any());
		 
	 }
	 
	 @Test
	 void delete_ShouldThrow_DatabaseIntegrityViolationException_WhenCityIsReferenced() {
		 
         Long referencedId = 99L;
		 
         when(repository.existsById(referencedId)).thenReturn(true);
         
         doThrow(DatabaseIntegrityViolationException.class).when(repository).deleteById(referencedId);
         
	     assertThrows(DatabaseIntegrityViolationException.class, () -> service.delete(referencedId));
	     verify(repository, never()).delete(any());
		 
	 }
	 
	 @Test
	 void update_ShouldSucceed_WhenCityExist() {
		 
		 Long validId = 5L;
		 City mockedCity = new City(validId,"Caçapava",State.SP);
		 CityDTO cityDTO = new CityDTO(mockedCity);
		 City updatedCity = new City(validId,"Caçapava Updated",State.SP);
		 
		 when(repository.getReferenceById(validId)).thenReturn(mockedCity);
		 when(repository.save(any(City.class))).thenReturn(updatedCity);
		 
		 CityDTO result = service.update(validId, cityDTO);
		 
		 Assertions.assertNotNull(result);
		 assertEquals(validId, result.id());
		 assertEquals(updatedCity.getName(), result.name());
	     verify(repository).getReferenceById(validId);
	 }
	 
	 @Test
	 void update_ShouldThrowResourceNotFoundException_WhenCityDoesNotExist() {
		 
		 Long nonExistentId = 99L;
		 City mockedCity = new City(nonExistentId,"Caçapava",State.SP);
		 CityDTO cityDTO = new CityDTO(mockedCity);
		 
		 when(repository.getReferenceById(nonExistentId)).thenThrow(ResourceNotFoundException.class);
	       
		 assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, cityDTO));
	     verify(repository, never()).save(any());
		 
	 }
	 
	 @Test
	    void findAllPaged_ShouldReturnSortedPagedOfCityDTOs() {
		 
		 City mockedCity = new City(1L,"Caçapava",State.SP);
		 List<City> content = List.of(mockedCity);
	     Pageable inputPageable = PageRequest.of(0, 5);
	     Page<City> page = new PageImpl<>(content);
	     Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("name").ascending());
	     
	     when(repository.findAll(expectedPageable)).thenReturn(page);
       
        Page<CityDTO> result = service.findAllPaged(inputPageable);
        
        assertEquals(1, result.getTotalElements());
	    assertEquals(mockedCity.getName(), result.getContent().get(0).name());
	    verify(repository).findAll(expectedPageable);
		 
	 }
	 
	 @Test
	    void findAll_ShouldReturnSortedListOfCityDTOs() {
		 
		 City mockedCity1 = new City(1L,"Caçapava",State.SP);
		 City mockedCity2 = new City(1L,"Jacareí",State.SP);
		 Sort sort = Sort.by("name");
		 List<City> cityList = List.of(mockedCity1, mockedCity2);
		 
		 when(repository.findAll(sort)).thenReturn(cityList);
		  
		  List<CityDTO> result = service.findAll();
		  
		  assertEquals(cityList.size(), result.size());
	      assertEquals(cityList.get(0).getName(), result.get(0).name());
	      assertEquals(cityList.get(1).getName(), result.get(1).name());
	      verify(repository).findAll(sort);
		 
	 }
	

}
