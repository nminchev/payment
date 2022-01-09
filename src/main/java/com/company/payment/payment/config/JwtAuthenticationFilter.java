package com.company.payment.payment.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.company.payment.payment.util.PaymentConstants;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private static Logger log = LogManager.getLogger(JwtAuthenticationFilter.class);

	@Value("${key.common}")
	private String keyCommon;

	public JwtAuthenticationFilter() {
		super("/**");
	}

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			throw new AuthenticationCredentialsNotFoundException("No JWT token found in request headers");
		}

		// remove Bearer prefix
		String jwtToken = header.substring(7);

		// decrypt the JWT token
		String decryptedToken = JWT.require(Algorithm.HMAC512(keyCommon)).build().verify(jwtToken).getSubject();
		if (decryptedToken != null) {
			String[] split = decryptedToken.split(PaymentConstants.TOKEN_SEPARATOR);

			// check for exactly 2 parameters in the jwt token
			if (split.length != 2) {
				throw new AuthenticationCredentialsNotFoundException("Invalid token");
			}

			String email = split[0];
			Integer merchantId = Integer.valueOf(split[1]);

			// pass the control to authentication provider
			JwtUsernamePasswordAuthenticationToken authentication = new JwtUsernamePasswordAuthenticationToken(email,
					jwtToken, merchantId);
			return getAuthenticationManager().authenticate(authentication);
		}

		return null;

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);

		chain.doFilter(request, response);

		log.info("Successfull authentication");
	}

}
