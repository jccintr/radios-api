package com.jcsoftware.radios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.dtos.CityDTO;
import com.jcsoftware.radios.entities.dtos.NewCityDTO;
import com.jcsoftware.radios.entities.enums.State;
import com.jcsoftware.radios.repositories.CityRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class CityServiceTests {
	
	@InjectMocks
	private CityService service;
	private City city;
	private CityDTO cityDTO;
	private List<City> cityList;
	private PageImpl<City> cityPage;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Pageable pageable;
	
	@Mock
	private CityRepository repository;
	
	@BeforeEach
	void setup() throws Exception {
		
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 10L;
		pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
		city = new City(1L,"São Paulo",State.SP);
		cityDTO = new CityDTO(city);
		cityPage = new PageImpl<>(List.of(city), pageable, 1);
		cityList = List.of(
	            new City(1L, "Campos do Jordão",State.SP),
	            new City(2L, "São Paulo",State.SP)
	    );
		
		when(repository.findAll(any(Sort.class))).thenReturn(cityList);
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(cityPage);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(city));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(city);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(city);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
	}
	
	@Test
	public void insert_ShouldReturnCityDTO() {
		NewCityDTO newCityDTO = new NewCityDTO("São Paulo",State.SP);
		CityDTO result = service.insert(newCityDTO);
		assertEquals(newCityDTO.name(), result.name());
		assertEquals(newCityDTO.state(), result.state());
        verify(repository, times(1)).save(any(City.class));
		
	}
	
	@Test
	public void findById_ShouldReturnCityDTOWhenIdExists() {
	    CityDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findById_ShouldThrowResourceNotFoundExceptionOWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.findById(nonExistingId);
		});
		
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
	public void update_ShouldReturnCityDTOWhenIdExists() {
		CityDTO result = service.update(existingId, cityDTO);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void update_ShouldThrowResourceNotFoundExceptionOWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.update(nonExistingId, cityDTO);
		});
	}
	
	@Test
    void findAllPaged_ShouldReturnPagedCategoryDTOs() {
       
        Page<CityDTO> result = service.findAllPaged(pageable);
       
        assertNotNull(result, "A página retornada não deve ser nula");
        assertEquals(1, result.getTotalElements(), "O total de elementos deve ser 1");
        assertEquals(1, result.getContent().size(), "A página deve conter 1 elemento");
        assertEquals("São Paulo", result.getContent().get(0).name(), "O nome da cidade deve ser 'São Paulo'");
        assertEquals(State.SP, result.getContent().get(0).state(), "O estado deve ser 'SP");
        
    }
	
	@Test
    void findAll_ShouldReturnSortedCategoryDTOs() {
        
        List<CityDTO> result = service.findAll();
        
        assertNotNull(result, "A lista retornada não deve ser nula");
        assertEquals(2, result.size(), "A lista deve conter 2 elementos");
        assertEquals("Campos do Jordão", result.get(0).name(), "A primeira cidade deve ser 'Campos do Jordão'");
        assertEquals("São Paulo", result.get(1).name(), "A segunda cidade deve ser 'São Paulo'");

    }


}
