package com.jcsoftware.radios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jcsoftware.radios.entities.Role;
import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.entities.dtos.RegisterDTO;
import com.jcsoftware.radios.entities.dtos.UserDTO;
import com.jcsoftware.radios.entities.dtos.UserUpdateDTO;
import com.jcsoftware.radios.entities.dtos.UserWithRolesDTO;
import com.jcsoftware.radios.repositories.RoleRepository;
import com.jcsoftware.radios.repositories.UserRepository;
import com.jcsoftware.radios.services.exceptions.DuplicatedEmailException;
import com.jcsoftware.radios.services.exceptions.ForbiddenException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;
import com.jcsoftware.radios.util.CustomUserUtil;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
	
	@Spy
	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
    private PasswordEncoder passwordEncoder;
	@Mock
	private CustomUserUtil customUserUtil;
	
	@AfterEach
	void clearSecurityContext() {
	    SecurityContextHolder.clearContext();
	}
	
	@Test
	public void loadUserByUsername_ShouldReturnUserDeails_WhenUserExist(){
		
		String existingUserName = "maria@gmail.com";
		User existingUser = new User(1L,"Maria",existingUserName,"123456");
		
		when(repository.findByEmail(existingUserName)).thenReturn(existingUser);
		
		UserDetails result = service.loadUserByUsername(existingUserName);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(),existingUserName);
		
	}
	
	@Test
	public void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExists() {
		
		String nonExistentUserName = "joyce@gmail.com";
		
		when(repository.findByEmail(nonExistentUserName)).thenReturn(null);
		
		Assertions.assertThrows(UsernameNotFoundException.class,()->{
			service.loadUserByUsername(nonExistentUserName);
		});
		verify(repository).findByEmail(nonExistentUserName);
		
	}
	
	@Test
	public void insert_ShouldPersistCityAndReturnUserDTO() {
		
		String uniqueEmail = "joyce@gmail.com";
		RegisterDTO registerDTO = new RegisterDTO("Joyce",uniqueEmail,"123456");
		String roleAuthority = "ROLE_COMMON";
		Role role = new Role(1L,"ROLE_COMMON");
		
		User savedUser = new User();
		savedUser.setName(registerDTO.name());
		savedUser.setEmail(registerDTO.email());
		savedUser.setPassword(registerDTO.password());
		savedUser.getRoles().add(role);
		
		when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedPassword");
		when(roleRepository.findByAuthority(roleAuthority)).thenReturn(role);
		when(repository.save(any(User.class))).thenReturn(savedUser);
		
		UserDTO result = service.insert(registerDTO);
		
		assertEquals(result.name(),registerDTO.name());
		assertEquals(result.email(),registerDTO.email());
		verify(repository).findByEmail(registerDTO.email());
	    verify(passwordEncoder).encode(registerDTO.password());
		verify(roleRepository).findByAuthority(roleAuthority);
        verify(repository, times(1)).save(any(User.class));
	}
	
	@Test
	public void insert_ShouldThrowDuplicatedEmailException_WhenEmailAlreadyExist() {
		
		
	    RegisterDTO dto = new RegisterDTO("Julio", "julio@email.com", "123456");

	   
	    User existingUser = new User();
	    existingUser.setId(10L);
	    existingUser.setEmail(dto.email());
	    when(repository.findByEmail(dto.email())).thenReturn(existingUser);

	   
	    assertThrows(DuplicatedEmailException.class, () -> service.insert(dto));
	    
	    verify(passwordEncoder, never()).encode(anyString());
	    verify(repository, never()).save(any(User.class));
	    verify(roleRepository, never()).findByAuthority(anyString());
		
	}
	
	@Test
	public void findAll_ShouldReturnSortedPageOf_UserWithRolesDTO() {
		
		Role mockedRole1 = new Role(1L,"ROLE_COMMON");
		Role mockedRole2 = new Role(1L,"ROLE_ADMIN");
		User mockedUser1 = new User(1L,"Joyce","joyce@gmail.com","123456");
		mockedUser1.getRoles().add(mockedRole1);
		User mockedUser2 = new User(1L,"Julio","julio@gmail.com","123456");
		mockedUser2.getRoles().add(mockedRole2);
		
		List<User> content = List.of(mockedUser1,mockedUser2);
	    Pageable inputPageable = PageRequest.of(0, 5);
	    Page<User> page = new PageImpl<>(content);
	    Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("name").ascending());
	    
	    when(repository.findAll(expectedPageable)).thenReturn(page);
	    
	    Page<UserWithRolesDTO> result = service.findAll(inputPageable);
	    
	    assertEquals(content.size(), result.getTotalElements());
	    assertEquals(content.get(0).getName(), result.getContent().get(0).name());
	    assertEquals(content.get(1).getEmail(), result.getContent().get(1).email());
	    assertEquals(result.getContent().get(0).roles().get(0).authority(),mockedRole1.getAuthority());
	    verify(repository).findAll(expectedPageable);
		
	}
	
	@Test
	public void findById_ShouldReturnUserWithRolesDTO_WhenUserExist() {
		
		Long existingId = 1L;
		Role mockedRole = new Role(1L,"ROLE_COMMON");
		User mockedUser = new User(1L,"Joyce","joyce@gmail.com","123456");
		mockedUser.getRoles().add(mockedRole);
		
		when(repository.findById(existingId)).thenReturn(Optional.of(mockedUser));
		UserWithRolesDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
	    assertEquals(mockedUser.getName(), result.name());
	    assertEquals(mockedUser.getEmail(), result.email());
	    assertEquals(mockedUser.getRoles().iterator().next().getAuthority(),result.roles().get(0).authority());
	    verify(repository).findById(existingId);
		
	}
	
	@Test
	 void findById_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
		
		Long nonExistentId = 99L;
		
		 when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
		 
		 Assertions.assertThrows(ResourceNotFoundException.class,()->{
				service.findById(nonExistentId);
			});
		verify(repository).findById(nonExistentId);
		
	}
	
	 @Test
	 void update_ShouldSucceed_WhenUserExist_And_UserLoggedIsAdmin() {
		 
		 Long existingId = 1L;
		 UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Joyce updated");
		 User existingUser = new User(existingId,"Joyce","joyce@gmail.com","123456");
		 User admin = new User();
		 admin.setId(99L);
		 admin.getRoles().add(new Role(1L, "ROLE_ADMIN"));
		 User savedUser = new User();
		 savedUser.setId(existingId);
		 savedUser.setName(userUpdateDTO.name());
		  
		 when(repository.findById(existingId)).thenReturn(Optional.of(existingUser));
		 when(repository.save(any(User.class))).thenReturn(savedUser);
		 doReturn(admin).when(service).me();
		 
		 UserDTO result = service.update(existingId, userUpdateDTO);
		 
		 assertNotNull(result);
		 assertEquals(existingId, result.id());
		 assertEquals(userUpdateDTO.name(), result.name());
		 verify(repository).findById(existingId);
		 verify(repository).save(any(User.class));
		 
	 }
	 
	 
	 @Test
	 void update_ShouldSucceed_WhenUserExist_And_UserLoggedIsOwner() {
		 
		 Long existingId = 1L;
		 UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Joyce updated");
		 User existingUser = new User(existingId,"Joyce","joyce@gmail.com","123456");
		 User loggedUser = new User();
		 loggedUser.setId(existingId);
		 User savedUser = new User();
		 savedUser.setId(existingId);
		 savedUser.setName(userUpdateDTO.name());
		 
		 when(repository.findById(existingId)).thenReturn(Optional.of(existingUser));
		 when(repository.save(any(User.class))).thenReturn(savedUser);
		 doReturn(loggedUser).when(service).me();
		 
		 UserDTO result = service.update(existingId, userUpdateDTO);
		 
		 assertNotNull(result);
		 assertEquals(existingId, result.id());
		 assertEquals(userUpdateDTO.name(), result.name());
		 verify(repository).findById(existingId);
		 verify(repository).save(any(User.class));
		 
	 }
	 
	 @Test
	 void updateId_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
		 
		 Long nonExistentId = 99L;
		 UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Joyce updated");
		 
		 when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
		 
		 assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, userUpdateDTO));
		 verify(repository, never()).save(any(User.class));
		 
		 
	 }
	 
	 @Test
	 void update_ShouldThrowForbiddenException_WhenLoggedUserIsNotAdminAndTriesToUpdateAnotherUser() {
		 
		 Long existingId = 1L;
		 UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Joyce updated");
		 User userToUpdate = new User();
		 userToUpdate.setId(existingId);
		 User loggedUser = new User();
		 loggedUser.setId(99L);
		 
		 when(repository.findById(existingId)).thenReturn(Optional.of(userToUpdate));
		 doReturn(loggedUser).when(service).me();
		 
		 assertThrows(ForbiddenException.class, () -> service.update(existingId, userUpdateDTO));
		 verify(repository, never()).save(any(User.class));
	 }
	 
	 @Test
	 public void delete_ShouldSucceed_WhenUserExist() {
        
		Long validId = 1L;
		
        when(repository.existsById(validId)).thenReturn(true);
		 
		 Assertions.assertDoesNotThrow(()->service.delete(validId));
		 
		 verify(repository).deleteById(validId);
		
	 }
	 
	 @Test
	 void delete_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
		 
         Long nonExistentId = 99L;
		 
		 when(repository.existsById(nonExistentId)).thenReturn(false);
		 
		 assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
	     verify(repository, never()).delete(any());
		 
	 }
	 
	 @Test
	 void auth_ShouldReturnUser_WhenUsernameExists() {
	    
	     String email = "julio@email.com";
	     User user = new User();
	     user.setId(1L);
	     user.setEmail(email);
	     
	     when(repository.findByEmail(email)).thenReturn(user);
	     when(customUserUtil.getLoggedUserName()).thenReturn(email);

	     
	     User result = service.auth();

	    
	     assertNotNull(result);
	     assertEquals(email, result.getEmail());
	     verify(customUserUtil).getLoggedUserName();
	     verify(repository).findByEmail(email);
	     
	 }
	 
	 @Test
	 void auth_ShouldThrowUsernameNotFoundException_WhenCustomUserUtilFails() {
	     
	    when(customUserUtil.getLoggedUserName()).thenThrow(new RuntimeException("Username not found"));

	    assertThrows(UsernameNotFoundException.class, () -> service.auth());
	    
	    verify(repository, never()).findByEmail(anyString());
	    
	 }
	 
	 @Test
	 void me_ShouldReturnUserFromAuth() {
	     
	     User user = new User();
	     user.setId(2L);
	     user.setEmail("joyce@email.com");

	     
	     doReturn(user).when(service).auth();

	     // Act
	     User result = service.me();

	     // Assert
	     assertNotNull(result);
	     assertEquals("joyce@email.com", result.getEmail());
	     verify(service).auth();
	     
	 }



	 
	

}
