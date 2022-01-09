package com.company.payment.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.company.payment.payment.config.login.JwtAuthenticationFilter;
import com.company.payment.payment.config.login.JwtAuthenticationProvider;
import com.company.payment.payment.config.login.JwtAuthenticationSuccessHandler;
import com.company.payment.payment.service.factory.AuthorizeAction;
import com.company.payment.payment.service.factory.ChargeAction;
import com.company.payment.payment.service.factory.RefundAction;
import com.company.payment.payment.service.factory.ReversalAction;
import com.company.payment.payment.service.factory.TransactionActionFactory;

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

	@Bean
	public TransactionActionFactory transactionActionFactory() {
		return new TransactionActionFactory();
	}

	@Bean
	public AuthorizeAction authorizeAction() {
		return new AuthorizeAction();
	}

	@Bean
	public ChargeAction chargeAction() {
		return new ChargeAction();
	}

	@Bean
	public RefundAction refundAction() {
		return new RefundAction();
	}

	@Bean
	public ReversalAction reversalAction() {
		return new ReversalAction();
	}

}