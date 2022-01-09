package com.company.payment.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class BeanConfig {

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
		jwtAuthenticationFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
		jwtAuthenticationFilter.setAllowSessionCreation(false);

		return jwtAuthenticationFilter;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		AuthenticationManager authenticationManager = new ProviderManager(userDetailsAuthenticationProvider());
		return authenticationManager;
	}

	@Bean
	public AbstractUserDetailsAuthenticationProvider userDetailsAuthenticationProvider() {
		return new JwtAuthenticationProvider();
	}

	@Bean
	public AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}

}