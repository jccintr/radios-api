package com.jcsoftware.radios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
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
import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.Radio;
import com.jcsoftware.radios.entities.dtos.CategoryDTO;
import com.jcsoftware.radios.entities.dtos.NewRadioDTO;
import com.jcsoftware.radios.entities.dtos.RadioDTO;
import com.jcsoftware.radios.entities.dtos.UpdateRadioDTO;
import com.jcsoftware.radios.entities.enums.State;
import com.jcsoftware.radios.repositories.CategoryRepository;
import com.jcsoftware.radios.repositories.CityRepository;
import com.jcsoftware.radios.repositories.RadioRepository;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RadioServiceTests {
	
	@InjectMocks
    private RadioService service;
	
	@Mock
    private RadioRepository repository;
	
	 @Mock
	 private CityRepository cityRepository;
	
	@Mock
    private CategoryRepository categoryRepository;
	
	@Test
    void insert_ShouldPersistRadioWithCityAndCategoriesAndReturnDTO() {
        // Arrange
        Long cityId = 1L;
        Long categoryId = 10L;

        City mockCity = new City(cityId, "São Paulo", State.SP);
        Category mockCategory = new Category(categoryId,"Gospel");
      

        when(cityRepository.getReferenceById(cityId)).thenReturn(mockCity);
        when(categoryRepository.getReferenceById(categoryId)).thenReturn(mockCategory);

        CategoryDTO categoryDTO = new CategoryDTO(categoryId, "Gospel");

        NewRadioDTO dto = new NewRadioDTO(
                "Rádio Teste",
                "radio_teste",
                "http://stream.teste.com/stream",
                "http://images.teste.com/image.jpg",
                true,
                cityId,
                List.of(categoryDTO)
        );

        // Simulando a entidade a ser salva
        Radio unsavedRadio = new Radio();
        unsavedRadio.setName(dto.name());
        unsavedRadio.setShortName(dto.shortName());
        unsavedRadio.setStreamUrl(dto.streamUrl());
        unsavedRadio.setImageUrl(dto.imageUrl());
        unsavedRadio.setHsl(dto.hsl());
        unsavedRadio.setCity(mockCity);
        unsavedRadio.getCategories().add(mockCategory);

        // Simulando a entidade já salva
        Radio savedRadio = new Radio();
        savedRadio.setId(100L);
        savedRadio.setName(dto.name());
        savedRadio.setShortName(dto.shortName());
        savedRadio.setStreamUrl(dto.streamUrl());
        savedRadio.setImageUrl(dto.imageUrl());
        savedRadio.setHsl(dto.hsl());
        savedRadio.setCity(mockCity);
        savedRadio.getCategories().add(mockCategory);

        when(repository.save(any(Radio.class))).thenReturn(savedRadio);

        // Act
        RadioDTO result = service.insert(dto);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.id());
        assertEquals("Rádio Teste", result.name());
        assertEquals("radio_teste", result.shortName());
        assertEquals("http://stream.teste.com/stream", result.streamUrl());
        assertEquals("http://images.teste.com/image.jpg", result.imageUrl());
        assertEquals(true, result.hsl());

        assertEquals("São Paulo", result.city().name());
        assertEquals("SP", result.city().state().name());

        assertEquals(1, result.categories().size());
        assertEquals("Gospel", result.categories().get(0).name());

        verify(cityRepository).getReferenceById(cityId);
        verify(categoryRepository).getReferenceById(categoryId);
        verify(repository).save(any(Radio.class));
        
    }
	
	 @Test
	 void findAllPaged_ShouldReturnPageOfRadioDTO() {
		 
		 Long cityId = 1L;
	     Long categoryId = 10L;
	     Long radioId = 20L;
		 City mockCity = new City(cityId, "Rio de Janeiro", State.RJ);
		 Category mockCategory = new Category(categoryId,"Gospel");
		 Radio mockRadio = new Radio(
				 radioId,
				 "Rádio 93 FM",
		         "93 FM",
		         "http://stream.teste.com/stream93fm",
		         "http://images.teste.com/93fm.jpg",
		         false,
		         mockCity);
		  mockRadio.getCategories().add(mockCategory);
		  
		  List<Radio> content = List.of(mockRadio);
	      Pageable inputPageable = PageRequest.of(0, 5);
	      Page<Radio> page = new PageImpl<>(content);
	      Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("name").ascending());
		  
	      when(repository.findAll(expectedPageable)).thenReturn(page);
	      
	      Page<RadioDTO> result = service.findAllPaged(inputPageable);
	      
	      assertEquals(1, result.getTotalElements());
	      assertEquals("Rádio 93 FM", result.getContent().get(0).name());
	      assertEquals("Rio de Janeiro", result.getContent().get(0).city().name());
	      assertEquals(1, result.getContent().get(0).categories().size());
	      assertEquals("Gospel", result.getContent().get(0).categories().get(0).name());
	      verify(repository).findAll(expectedPageable);
	 }
	 
	 @Test
	 void findAll_ShouldReturnListOfRadioDTO() {
		 
		 City mockCity1 = new City(1L, "Rio de Janeiro", State.RJ);
		 Category mockCategory1 = new Category(1L,"Gospel");
		 City mockCity2 = new City(1L, "Campos do Jordão", State.SP);
		 Category mockCategory2 = new Category(1L,"MPB");
		 
		 Radio mockRadio1 = new Radio(
				 1L,
				 "Rádio 93 FM",
		         "93 FM",
		         "http://stream.teste.com/stream93fm",
		         "http://images.teste.com/93fm.jpg",
		         false,
		         mockCity1);
		  mockRadio1.getCategories().add(mockCategory1);
		  
		  Radio mockRadio2 = new Radio(
					 1L,
					 "Band Vale FM",
			         "Band Vale",
			         "http://stream.teste.com/stream_band_valefm",
			         "http://images.teste.com/bandvale.jpg",
			         false,
			         mockCity2);
		  mockRadio2.getCategories().add(mockCategory2);
			  
		  Sort sort = Sort.by("name");
		  List<Radio> radioList = List.of(mockRadio1, mockRadio2);
		  when(repository.findAll(sort)).thenReturn(radioList);
		  
		  List<RadioDTO> result = service.findAll();
		  
		  assertEquals(radioList.size(), result.size());
	      assertEquals(radioList.get(0).getName(), result.get(0).name());
	      assertEquals(radioList.get(0).getCategories().size(), result.get(0).categories().size());
	      assertEquals(radioList.get(0).getCity().getName(), result.get(0).city().name());
	      assertEquals(radioList.get(1).getName(), result.get(1).name());
	      assertEquals(radioList.get(1).getCity().getName(), result.get(1).city().name());
	      assertEquals(radioList.get(1).getCategories().size(), result.get(1).categories().size());
	      verify(repository).findAll(sort);
		 
	 }
	 
	 @Test
	 void findById_ShouldReturnRadioDTO_WhenRadioExist() {
		 
		 Long existingRadioId = 1L;
		 City mockCity = new City(1L, "Rio de Janeiro", State.RJ);
		 Category mockCategory = new Category(1L,"Gospel");
		 Radio mockRadio = new Radio(
				 1L,
				 "Rádio 93 FM",
		         "93 FM",
		         "http://stream.teste.com/stream93fm",
		         "http://images.teste.com/93fm.jpg",
		         false,
		         mockCity);
		  mockRadio.getCategories().add(mockCategory);
		  
		  when(repository.findById(existingRadioId)).thenReturn(Optional.of(mockRadio));
		  
		  RadioDTO result = service.findById(existingRadioId);
		  
		  assertNotNull(result);
	      assertEquals(existingRadioId, result.id());
	      assertEquals(mockRadio.getName(), result.name());
	      assertEquals(mockRadio.getCity().getName(),result.city().name());
	      assertEquals(mockRadio.getCategories().size(),result.categories().size());
          verify(repository).findById(existingRadioId);
	 }
	 
	 
	 @Test
	 void findById_ShouldThrowResourceNotFoundException_WhenRadioDoesNotExist() {
		 
		 Long nonExistentId = 99L;
		 
		 when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
		 
		 assertThrows(ResourceNotFoundException.class, () -> {
	            service.findById(nonExistentId);
	     });

	     verify(repository).findById(nonExistentId);
	 }
	 
	 @Test
	 void delete_ShouldSucceed_WhenRadioExistsAndIsNotReferenced() {
		 
		 Long validId = 99L;
		 
		 when(repository.existsById(validId)).thenReturn(true);
		 
		 Assertions.assertDoesNotThrow(()->service.delete(validId));
		 
		 verify(repository).deleteById(validId);
		 
	 }
	 
	 
	 @Test
	 void delete_ShouldThrow_DatabaseIntegrityViolationException_WhenRadioIsReferenced() {
		 Long referencedId = 99L;
         when(repository.existsById(referencedId)).thenReturn(true);
         doThrow(DatabaseIntegrityViolationException.class).when(repository).deleteById(referencedId);
	     assertThrows(DatabaseIntegrityViolationException.class, () -> service.delete(referencedId));
	     verify(repository, never()).delete(any());
	 }
	 
	 @Test
	 void delete_ShouldThrowResourceNotFoundException_WhenRadioDoesNotExist() {
		 
		 Long nonExistentId = 99L;
		 
		 when(repository.existsById(nonExistentId)).thenReturn(false);
		 
		 assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
	     verify(repository, never()).delete(any());
	 }
	 
	 @Test
	 void update_ShouldSucceed_WhenRadioExist() {
		   Long categoryId = 1L;
		   Long categoryId2 = 2L;
		   Long existingRadioId = 1L;
		   City mockCity = new City(1L, "Rio de Janeiro", State.RJ);
	       Category mockCategory = new Category(categoryId,"Gospel");
	       Category mockCategory2 = new Category(categoryId2,"Notícias");
	       CategoryDTO mockCategoryDTO = new CategoryDTO(mockCategory);
	       CategoryDTO mockCategoryDTO2 = new CategoryDTO(mockCategory2);
	       
	       Radio mockRadio = new Radio(
	    		   existingRadioId,
				 "Rádio 93 FM",
		         "93 FM",
		         "http://stream.teste.com/stream93fm",
		         "http://images.teste.com/93fm.jpg",
		         false,
		         mockCity);
		   mockRadio.getCategories().add(mockCategory);
		   
		   UpdateRadioDTO updateDto = new UpdateRadioDTO(
				   "Rádio 93 FM Updated",
				   "93 FM",
				   "http://stream.teste.com/stream93fm",
			       "http://images.teste.com/93fm.jpg",
			       false,
			       1L,
			       List.of(mockCategoryDTO,mockCategoryDTO2)
				   );
		   
		   Radio updatedRadio = new Radio();
		   updatedRadio.setId(existingRadioId);
		   updatedRadio.setName(updateDto.name());
		   updatedRadio.setShortName(updateDto.shortName());
		   updatedRadio.setStreamUrl(updateDto.streamUrl());
		   updatedRadio.setImageUrl(updateDto.imageUrl());
		   updatedRadio.setHsl(updateDto.hsl());
		   updatedRadio.setCity(mockCity);
		   updatedRadio.getCategories().add(mockCategory);
		   updatedRadio.getCategories().add(mockCategory2);
		   
		   when(repository.getReferenceById(existingRadioId)).thenReturn(mockRadio);
		   when(categoryRepository.getReferenceById(categoryId)).thenReturn(mockCategory);
		   when(categoryRepository.getReferenceById(categoryId2)).thenReturn(mockCategory2);
		   when(repository.save(any(Radio.class))).thenReturn(updatedRadio);
		   
		   RadioDTO result = service.update(existingRadioId, updateDto);
		   
		   assertNotNull(result);
		   assertEquals(existingRadioId, result.id());
		   assertEquals(updateDto.name(), result.name());
		   assertEquals(updateDto.categories().size(),result.categories().size());
	       verify(repository).getReferenceById(existingRadioId);
	 }
	 
	 @Test
	 void update_ShouldThrowResourceNotFoundException_WhenRadioDoesNotExist() {
		 
		 Long nonExistentId = 99L;
		 Category mockCategory = new Category(1L,"Gospel");
		 CategoryDTO mockCategoryDTO = new CategoryDTO(mockCategory);
		 UpdateRadioDTO updateDto = new UpdateRadioDTO(
				   "Rádio 93 FM Updated",
				   "93 FM",
				   "http://stream.teste.com/stream93fm",
			       "http://images.teste.com/93fm.jpg",
			       false,
			       1L,
			       List.of(mockCategoryDTO)
				   );

		 when(repository.getReferenceById(nonExistentId)).thenThrow(ResourceNotFoundException.class);
       
		 assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, updateDto));
	     verify(repository, never()).save(any());
	 }
	 

}
	