package com.company.payment.payment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.company.payment.payment.util.PaymentConstants;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		// register JWT Authentication Filter
		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		// no need for CSRF for single POST request
		httpSecurity.csrf().disable();

		// ROLE_MERCHANT authorization
		httpSecurity.authorizeRequests().antMatchers("/api/transaction").hasAuthority(PaymentConstants.ROLE_MERCHANT);

		// no need for session for single POST request
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
