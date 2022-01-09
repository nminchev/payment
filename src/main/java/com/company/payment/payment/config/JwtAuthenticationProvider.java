package com.company.payment.payment.config;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.MerchantStatus;
import com.company.payment.payment.model.repository.MerchantRepository;
import com.company.payment.payment.util.PaymentConstants;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static Logger log = LogManager.getLogger(JwtAuthenticationProvider.class);

	@Autowired
	private MerchantRepository merchantRepository;

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		try {
			JwtUsernamePasswordAuthenticationToken jwtAuthenticationToken = (JwtUsernamePasswordAuthenticationToken) authentication;

			// Retrieve the token from DB
			Merchant merchant = merchantRepository.retrieveMerchantById(jwtAuthenticationToken.getMerchantId());
			if (merchant == null) {
				throw new AuthenticationCredentialsNotFoundException("Merchant not found.");
			}

			// check for email match
			String email = merchant.getEmail();
			if (!email.equals(username)) {
				throw new AuthenticationCredentialsNotFoundException("Invalid token");
			}

			// check for active merchant
			if (merchant.getStatus() != MerchantStatus.ACTIVE) {
				throw new DisabledException("Merchant not ACTIVE");
			}

			// construct UserDetails object
			List<GrantedAuthority> authorityList = AuthorityUtils
					.commaSeparatedStringToAuthorityList(PaymentConstants.ROLE_MERCHANT);
			String jwtToken = (String) jwtAuthenticationToken.getCredentials();
			UserDetails userDetails = new User(email, jwtToken, authorityList);

			log.info("Successfully retrived merchant");
			return userDetails;

		} catch (Exception e) {
			throw new AuthenticationCredentialsNotFoundException("Invalid token", e);
		}

	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// no additional checks
	}
}
