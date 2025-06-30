package com.jcsoftware.radios.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	@Autowired
	UserService userService;
	
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
