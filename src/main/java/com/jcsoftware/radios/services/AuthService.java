package com.jcsoftware.radios.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jcsoftware.radios.entities.User;
import com.jcsoftware.radios.entities.dtos.LoginRequest;
import com.jcsoftware.radios.entities.dtos.LoginResponse;
import com.jcsoftware.radios.services.exceptions.ForbiddenException;
import com.jcsoftware.radios.services.exceptions.InvalidCredentialsException;

@Service
public class AuthService {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	 private RestTemplate restTemplate;
	
	@Value("${security.client-id}")
	private String clientId;

	@Value("${security.client-secret}")
	private String clientSecret;
	
	@Value("${oauth.login.url}")
	private String loginURL;
	
	public void isAdminOrOwner(Long userId,Long resource) {
		
		User me = userService.auth();
		
		if(me.hasRole("ROLE_ADMIN")) return;
		
		if(!me.getId().equals(userId))	throw new ForbiddenException(resource);
		
		
	}
	
	public void isOwner(Long userId,Long resource) {
		
		User me = userService.auth();
		
		if(!me.getId().equals(userId))	throw new ForbiddenException(resource);
		
		return;
	}

	public LoginResponse login(LoginRequest loginRequest) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(clientId,clientSecret);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("username", loginRequest.email());
		body.add("password", loginRequest.password());
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
			
		try {
		        ResponseEntity<Map> response = restTemplate.postForEntity(
		            loginURL,
		            request,
		            Map.class
		        );

		        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
		            Object token = response.getBody().get("access_token");
		            if (token instanceof String accessToken) {
		                return new LoginResponse(accessToken);
		            }
		        }

		        throw new InvalidCredentialsException("Invalid credentials");

		 } catch (HttpClientErrorException e) {
		        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
		            throw new InvalidCredentialsException("Invalid credentials");
		        }
		        throw new RuntimeException("Erro ao autenticar: " + e.getMessage(), e);
		    }
			
	}
	


}
