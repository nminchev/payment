package com.company.payment.payment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
        .authorizeRequests()
          .antMatchers("/").permitAll()
          .antMatchers("/user/**").hasAuthority("ROLE_ADMIN").anyRequest().authenticated()
          .and()
       .formLogin()
         .permitAll()
         .and()
      .logout()
      	.logoutUrl("/logout")
      	.logoutSuccessUrl("/")
        .permitAll()
        .and()
      .httpBasic();
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/ws/**");
	}
	
}
