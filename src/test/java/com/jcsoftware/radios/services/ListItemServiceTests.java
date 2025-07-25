package com.jcsoftware.radios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jcsoftware.radios.entities.City;
import com.jcsoftware.radios.entities.ListItem;
import com.jcsoftware.radios.entities.Radio;
import com.jcsoftware.radios.entities.RadioList;
import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.entities.dtos.ListItemDTO;
import com.jcsoftware.radios.entities.dtos.NewListItemDTO;
import com.jcsoftware.radios.entities.enums.State;
import com.jcsoftware.radios.repositories.ListItemRepository;
import com.jcsoftware.radios.repositories.RadioListRepository;
import com.jcsoftware.radios.repositories.RadioRepository;
import com.jcsoftware.radios.services.exceptions.ForbiddenException;
import com.jcsoftware.radios.services.exceptions.RadioAlreadyInListException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;
import com.jcsoftware.radios.tests.Factory;

@ExtendWith(MockitoExtension.class)
public class ListItemServiceTests {
	
	@InjectMocks
    private ListItemService service;

    @Mock
    private ListItemRepository repository;
    
    @Mock
    private RadioListRepository radioListRepository;
    
    @Mock
    private RadioRepository radioRepository;

    @Mock
    private AuthService authService;

    private Long validId;

	
    @BeforeEach
    void setup() {
       validId = 1L;
    }
    
 // 1. Lista não existe → ResourceNotFoundException
    @Test
    void insert_ShouldThrowResourceNotFoundException_WhenListNotFound() {
        NewListItemDTO dto = new NewListItemDTO(1L, 10L);
        when(radioListRepository.findById(dto.listId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.insert(dto));

        verify(radioListRepository).findById(dto.listId());
        verifyNoInteractions(authService, radioRepository, repository);
    }
    
 // 2. User não é dono da lista → ForbiddenException
    @Test
    void insert_ShouldThrowForbiddenException_WhenUserIsNotOwner() {
        Long listId = 1L;
        Long ownerId = 99L;

        RadioList radioList = new RadioList();
        radioList.setId(listId);
        radioList.setOwner(new User(ownerId, "Ana", "ana@email.com", "123"));

        NewListItemDTO dto = new NewListItemDTO(listId, 10L);

        when(radioListRepository.findById(listId)).thenReturn(Optional.of(radioList));
        doThrow(new ForbiddenException(listId)).when(authService).isOwner(ownerId, listId);

        assertThrows(ForbiddenException.class, () -> service.insert(dto));

        verify(authService).isOwner(ownerId, listId);
        verifyNoInteractions(radioRepository, repository);
    }
    
 // 3. Rádio não existe → ResourceNotFoundException
    @Test
    void insert_ShouldThrowResourceNotFoundException_WhenRadioNotFound() {
        Long listId = 1L;
        Long radioId = 10L;

        RadioList radioList = new RadioList();
        radioList.setId(listId);
        radioList.setOwner(new User(5L, "Don", "don@email.com", "123"));

        NewListItemDTO dto = new NewListItemDTO(listId, radioId);

        when(radioListRepository.findById(listId)).thenReturn(Optional.of(radioList));
        doNothing().when(authService).isOwner(anyLong(), anyLong());
        when(radioRepository.findById(radioId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.insert(dto));
    }
    
 // 4. Rádio já está na lista → RadioAlreadyInListException
    @Test
    void insert_ShouldThrowRadioAlreadyInListException_WhenRadioAlreadyInList() {
        Long listId = 1L;
        Long radioId = 10L;

        Radio radio = new Radio();
        radio.setId(radioId);

        RadioList radioList = new RadioList() {
            @Override
            public boolean containsRadio(Radio r) {
                return true; // simula que a rádio já está na lista
            }
        };
        radioList.setId(listId);
        radioList.setOwner(new User(5L, "Leo", "leo@email.com", "123"));

        NewListItemDTO dto = new NewListItemDTO(listId, radioId);

        when(radioListRepository.findById(listId)).thenReturn(Optional.of(radioList));
        doNothing().when(authService).isOwner(anyLong(), anyLong());
        when(radioRepository.findById(radioId)).thenReturn(Optional.of(radio));

        assertThrows(RadioAlreadyInListException.class, () -> service.insert(dto));
    }
    
   
    @Test
    void insert_ShouldReturnListItemDTO_WhenDataIsValid() {
        Long listId = 1L;
        Long radioId = 10L;

        // Dono da lista
        User owner = new User(5L, "Leo", "leo@email.com", "123");

        // Mock da lista com owner
        RadioList radioList = new RadioList() {
            @Override
            public boolean containsRadio(Radio radio) {
                return false;
            }
        };
        radioList.setId(listId);
        radioList.setOwner(owner);
        // cidade da rádio
        City city = new City(1L,"Campos do Jordão",State.SP);
        // Mock da rádio
        Radio radio = new Radio();
        radio.setId(radioId);
        radio.setCity(city);
        radio.setName("Rádio Alpha");

        // DTO de entrada
        NewListItemDTO dto = new NewListItemDTO(listId, radioId);

        // Mock do item a salvar
        ListItem savedItem = new ListItem();
        savedItem.setId(999L);
        savedItem.setList(radioList);
        savedItem.setRadio(radio);

        
        when(radioListRepository.findById(listId)).thenReturn(Optional.of(radioList));
        doNothing().when(authService).isOwner(owner.getId(), listId);
        when(radioRepository.findById(radioId)).thenReturn(Optional.of(radio));
        when(repository.save(any(ListItem.class))).thenReturn(savedItem);

        
        ListItemDTO result = service.insert(dto);

        
        assertNotNull(result);
        assertEquals(savedItem.getId(), result.id());
        assertNotNull(result.radio());
        assertEquals(radio.getId(), result.radio().id());
        assertEquals(radio.getName(), result.radio().name());

        verify(repository).save(any(ListItem.class));
    }

   
	
	
	@Test
	public void delete_ShouldDeleteWhenIdExistsAndUserIsOwner() {
		
		Long validId = 1L;
		ListItem listItem = Factory.createListItem();
		Mockito.when(repository.findById(validId)).thenReturn(Optional.of(listItem));
		
		Assertions.assertDoesNotThrow(()->{
			service.delete(validId);
		});
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(validId);
		
	}
	
	
	@Test
    public void delete_ShouldThrowResourceNotFoundException_WhenItemDoesNotExist() {
		
		Long invalidId = 1500L;
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.delete(invalidId);
		});
		
	}
	
	
    @Test
    void delete_ShouldThrowForbiddenException_WhenUserIsNotOwner() {
       
        Long listItemId = 1L;
        Long ownerId = 100L;
       

        User owner = new User();
        owner.setId(ownerId);

        RadioList radioList = new RadioList();
        radioList.setId(500L);
        radioList.setOwner(owner);

        ListItem listItem = new ListItem();
        listItem.setId(listItemId);
        listItem.setList(radioList);

        when(repository.findById(listItemId)).thenReturn(Optional.of(listItem));

       
        doThrow(new ForbiddenException(radioList.getId()))
                .when(authService).isOwner(ownerId, radioList.getId());

        
        assertThrows(ForbiddenException.class, () -> service.delete(listItemId));

       
        verify(repository, never()).deleteById(anyLong());
    }
}
