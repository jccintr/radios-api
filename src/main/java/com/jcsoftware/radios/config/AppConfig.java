package com.jcsoftware.radios.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	    	return new BCryptPasswordEncoder();
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
