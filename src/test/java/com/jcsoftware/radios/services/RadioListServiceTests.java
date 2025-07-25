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

import java.util.List;
import java.util.Optional;

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

import com.jcsoftware.radios.entities.RadioList;
import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.entities.dtos.NewRadioListDTO;
import com.jcsoftware.radios.entities.dtos.RadioListDTO;
import com.jcsoftware.radios.repositories.RadioListRepository;
import com.jcsoftware.radios.services.exceptions.ForbiddenException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RadioListServiceTests {

	@InjectMocks
    private RadioListService service;
	
	@Mock
    private RadioListRepository repository;
	
	@Mock
	private AuthService authService;
	
	 @Mock
	 private UserService userService;
	
	 @BeforeEach
     void setup() {
      
     }
	 
	// Teste 1: deve retornar RadioListDTO quando for owner ou admin
	 @Test
	    void findById_ShouldReturnRadioListDTO_WhenUserIsOwnerOrAdmin() {
	        Long radioListId = 1L;
	        Long ownerId = 100L;

	        User owner = new User();
	        owner.setId(ownerId);
	        owner.setName("Ana");
	        owner.setEmail("ana@email.com");

	        RadioList radioList = new RadioList();
	        radioList.setId(radioListId);
	        radioList.setName("Minhas Rádios Favoritas");
	        radioList.setOwner(owner);
	       

	        when(repository.findById(radioListId)).thenReturn(Optional.of(radioList));
	        doNothing().when(authService).isAdminOrOwner(ownerId, radioListId);

	        RadioListDTO result = service.findById(radioListId);

	        assertNotNull(result);
	        assertEquals(radioListId, result.id());
	        assertEquals("Minhas Rádios Favoritas", result.name());
	        assertEquals(ownerId, result.owner().id());

	        verify(repository).findById(radioListId);
	        verify(authService).isAdminOrOwner(ownerId, radioListId);
	    }
	 
	// Teste 2: deve lançar ResourceNotFoundException quando a lista não existir
	    @Test
	    void findById_ShouldThrowResourceNotFoundException_WhenListDoesNotExist() {
	        Long nonExistentId = 99L;

	        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

	        assertThrows(ResourceNotFoundException.class, () -> {
	            service.findById(nonExistentId);
	        });

	        verify(repository).findById(nonExistentId);
	        verifyNoInteractions(authService);
	    }
	    
	    // Teste 3: deve lançar ForbiddenException quando o usuário não for owner nem admin
	    @Test
	    void findById_ShouldThrowForbiddenException_WhenUserIsNotOwnerOrAdmin() {
	        Long listId = 2L;
	        Long ownerId = 300L;

	        User owner = new User();
	        owner.setId(ownerId);

	        RadioList radioList = new RadioList();
	        radioList.setId(listId);
	        radioList.setOwner(owner);

	        when(repository.findById(listId)).thenReturn(Optional.of(radioList));
	        doThrow(new ForbiddenException(listId)).when(authService).isAdminOrOwner(ownerId, listId);

	        assertThrows(ForbiddenException.class, () -> {
	            service.findById(listId);
	        });

	        verify(repository).findById(listId);
	        verify(authService).isAdminOrOwner(ownerId, listId);
	    }
	    
	    @Test
	    void insert_ShouldReturnRadioListDTO_WhenDataIsValid() {
	        // Arrange
	        String listName = "Minha Lista";
	        NewRadioListDTO dto = new NewRadioListDTO(listName);

	        User owner = new User();
	        owner.setId(1L);
	        owner.setName("Julio");
	        owner.setEmail("julio@email.com");

	        RadioList radioListSaved = new RadioList();
	        radioListSaved.setId(10L);
	        radioListSaved.setName(listName);
	        radioListSaved.setOwner(owner);
	        

	        when(userService.me()).thenReturn(owner);
	        when(repository.save(any(RadioList.class))).thenReturn(radioListSaved);

	        // Act
	        RadioListDTO result = service.insert(dto);

	        // Assert
	        assertNotNull(result);
	        assertEquals(radioListSaved.getId(), result.id());
	        assertEquals(listName, result.name());
	        assertEquals(owner.getId(), result.owner().id());
	        assertEquals(owner.getName(), result.owner().name());
	        assertEquals(owner.getEmail(), result.owner().email());

	        verify(userService).me();
	        verify(repository).save(any(RadioList.class));
	    }
	    
	    @Test
	    void findAll_ShouldReturnPageOfRadioListDTO() {
	        // Arrange
	        RadioList radioList = new RadioList();
	        radioList.setId(1L);
	        radioList.setName("Minhas Rádios");
	        radioList.setOwner(new User(1L, "João", "joao@email.com", "123456"));
	       

	        List<RadioList> content = List.of(radioList);
	        Pageable inputPageable = PageRequest.of(0, 5); // entrada
	        Page<RadioList> page = new PageImpl<>(content);

	        // Espera que o método sobrescreva para size 10 e sort by name asc
	        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("name").ascending());

	        when(repository.findAll(expectedPageable)).thenReturn(page);

	        // Act
	        Page<RadioListDTO> result = service.findAll(inputPageable);

	        // Assert
	        assertEquals(1, result.getTotalElements());
	        assertEquals("Minhas Rádios", result.getContent().get(0).name());
	        verify(repository).findAll(expectedPageable); // confirma que o pageable foi modificado corretamente
	    }
	    
	    @Test
	    void findAllByOwner_ShouldReturnListOfRadioListDTO() {
	        // Arrange
	        Long ownerId = 1L;
	        User owner = new User(ownerId, "Maria", "maria@email.com", "senha123");
	        when(userService.me()).thenReturn(owner);

	        RadioList radioList1 = new RadioList();
	        radioList1.setId(10L);
	        radioList1.setName("Favoritas");
	        radioList1.setOwner(owner);
	       

	        RadioList radioList2 = new RadioList();
	        radioList2.setId(20L);
	        radioList2.setName("Trabalho");
	        radioList2.setOwner(owner);
	        

	        List<RadioList> mockResult = List.of(radioList1, radioList2);
	        when(repository.findByOwnerId(ownerId)).thenReturn(mockResult);

	        // Act
	        List<RadioListDTO> result = service.findAllByOwner();

	        // Assert
	        assertEquals(2, result.size());
	        assertEquals("Favoritas", result.get(0).name());
	        assertEquals("Trabalho", result.get(1).name());
	        verify(userService).me();
	        verify(repository).findByOwnerId(ownerId);
	    }
	    
	    @Test
	    void delete_ShouldSucceed_WhenUserIsOwnerAndRadioListExist() {
	        // Arrange
	        Long listId = 1L;
	        User owner = new User(100L, "João", "joao@email.com", "123456");
	        RadioList radioList = new RadioList();
	        radioList.setId(listId);
	        radioList.setOwner(owner);

	        when(repository.findById(listId)).thenReturn(Optional.of(radioList));

	        // Act
	        service.delete(listId);

	        // Assert
	        verify(authService).isOwner(owner.getId(), listId);
	        verify(repository).delete(radioList);
	    }

	    @Test
	    void delete_ShouldThrowResourceNotFoundException_WhenListDoesNotExist() {
	        // Arrange
	        Long listId = 99L;
	        when(repository.findById(listId)).thenReturn(Optional.empty());

	        // Act & Assert
	        assertThrows(ResourceNotFoundException.class, () -> service.delete(listId));
	        verify(repository, never()).delete(any());
	        verify(authService, never()).isOwner(anyLong(), anyLong());
	    }

	    @Test
	    void delete_ShouldThrowForbiddenException_WhenUserIsNotOwner() {
	        // Arrange
	        Long listId = 1L;
	        User owner = new User(100L, "Maria", "maria@email.com", "senha123");
	        RadioList radioList = new RadioList();
	        radioList.setId(listId);
	        radioList.setOwner(owner);

	        when(repository.findById(listId)).thenReturn(Optional.of(radioList));
	        doThrow(new ForbiddenException("Access denied")).when(authService).isOwner(owner.getId(), listId);

	        // Act & Assert
	        assertThrows(ForbiddenException.class, () -> service.delete(listId));
	        verify(repository, never()).delete(any());
	    }
	    
	    @Test
	    void update_ShouldSucceed_WhenUserIsOwnerAndRadioListExist() {
	        // Arrange
	        Long listId = 1L;
	        User owner = new User(100L, "Carlos", "carlos@email.com", "senha123");
	        RadioList radioList = new RadioList();
	        radioList.setId(listId);
	        radioList.setName("Original Name");
	        radioList.setOwner(owner);

	        NewRadioListDTO dto = new NewRadioListDTO("Updated Name");

	        when(repository.findById(listId)).thenReturn(Optional.of(radioList));

	        // Act
	        RadioListDTO result = service.update(listId, dto);

	        // Assert
	        assertEquals(dto.name(), result.name());
	        verify(authService).isOwner(owner.getId(), listId);
	    }

	    @Test
	    void update_ShouldThrowResourceNotFoundException_WhenListDoesNotExist() {
	        // Arrange
	        Long listId = 404L;
	        NewRadioListDTO dto = new NewRadioListDTO("Updated Name");

	        when(repository.findById(listId)).thenReturn(Optional.empty());

	        // Act & Assert
	        assertThrows(ResourceNotFoundException.class, () -> service.update(listId, dto));
	        verify(authService, never()).isOwner(anyLong(), anyLong());
	    }

	    @Test
	    void update_ShouldThrowForbiddenException_WhenUserIsNotOwner() {
	        // Arrange
	        Long listId = 1L;
	        User owner = new User(101L, "Ana", "ana@email.com", "123456");
	        RadioList radioList = new RadioList();
	        radioList.setId(listId);
	        radioList.setName("Original Name");
	        radioList.setOwner(owner);

	        NewRadioListDTO dto = new NewRadioListDTO("Updated Name");

	        when(repository.findById(listId)).thenReturn(Optional.of(radioList));
	        doThrow(new ForbiddenException("Access denied")).when(authService).isOwner(owner.getId(), listId);

	        // Act & Assert
	        assertThrows(ForbiddenException.class, () -> service.update(listId, dto));
	        verify(repository, never()).save(any());
	    }


}
