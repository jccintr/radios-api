package com.jcsoftware.radios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import jakarta.validation.Valid;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}
	
	protected User auth() {
		
		try {
	        String username = getLoggedUserName();
			return repository.findByEmail(username);
		} catch (Exception e) {
			throw new UsernameNotFoundException("Enmail not found.");
		}
	}
	
	private String getLoggedUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
		return jwtPrincipal.getClaim("username");
		
	}
	
	@Transactional(readOnly=true)
	public User me() {

		return auth();
		
		
	}

	@Transactional
	public UserDTO insert(RegisterDTO dto) {
		
        User user = repository.findByEmail(dto.email());
		if(user!=null) {
			throw new DuplicatedEmailException();
		}
		
		User newUser = new User();
		newUser.setName(dto.name());
		newUser.setEmail(dto.email());
		newUser.setPassword(passwordEncoder.encode(dto.password()));
		newUser.getRoles().clear();
		Role role = roleRepository.findByAuthority("ROLE_COMMON");
		newUser.getRoles().add(role);
		newUser = repository.save(newUser);
		
		return new UserDTO(newUser);
		
	}
	
	public List<UserWithRolesDTO> findAll() {
		List<User> users = repository.findAll(Sort.by("name"));
		return users.stream().map(UserWithRolesDTO::new).toList();
	}
	
	public UserWithRolesDTO findById(Long id) {
		Optional<User> userO = repository.findById(id);
		User user = userO.orElseThrow(() -> new ResourceNotFoundException("User not found id: "+ id));
		
		return new UserWithRolesDTO(user);
	}

	public UserDTO update(Long id, @Valid UserUpdateDTO dto) {
		
		Optional<User> userO = repository.findById(id);
		User user = userO.orElseThrow(() -> new ResourceNotFoundException("User not found id: " + id));
		User me = this.me();
		if(!me.hasRole("ROLE_ADMIN") && me.getId()!= id) throw new ForbiddenException(id);
		// só pode alterar se for admin 
		// se não for admin, só altera se o id do user logado for igual ao id do user encontrado
		user.setName(dto.name());
		user = repository.save(user);
		return new UserDTO(user);
		
	}
	
	

}
