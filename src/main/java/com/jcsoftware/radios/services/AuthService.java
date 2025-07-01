package com.jcsoftware.radios.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.services.exceptions.ForbiddenException;

@Service
public class AuthService {
	
	@Autowired
	UserService userService;
	
	public void isAdminOrOwner(Long userId,Long resource) {
		
		User me = userService.auth();
		
		if(me.hasRole("ROLE_ADMIN")) return;
		
		if(!me.getId().equals(userId))	throw new ForbiddenException(resource);
		
		
	}
	
	/*
public void validateSelfOrAdmin(Long userId,Long resource) {
		
		User me = userService.authenticated();
		
		if(me.hasRole("ROLE_ADMIN")) {
			return;
		}
		if(!me.getId().equals(userId)) {
			throw new ForbiddenException(resource);
		}
		
	}
*/

}
