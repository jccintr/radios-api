package com.jcsoftware.radios.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	 @Bean
     public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
     }
	 
	 @Bean
	 public RestTemplate restTemplate() {
		    return new RestTemplate();
	 }
	    /*
	    @Bean
		@Profile("test")
		@Order(1)
		public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {

			http.securityMatcher(PathRequest.toH2Console()).csrf(csrf -> csrf.disable())
					.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
			return http.build();
		}
		*/

}
