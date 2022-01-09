package com.company.payment.payment.config.login;

import java.security.PrivateKey;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.MerchantStatus;
import com.company.payment.payment.model.repository.MerchantRepository;
import com.company.payment.payment.util.PaymentConstants;
import com.company.payment.payment.util.PaymentUtils;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static Logger log = LogManager.getLogger(JwtAuthenticationProvider.class);

	@Autowired
	private MerchantRepository merchantRepository;

	@Value("${key.private.folder}")
	private String keyPrivateFolder;

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

			PrivateKey privateKey = getMerchantPrivateKey(merchant);
			UserDetails userDetails = new PaymentUserDetails(email, jwtToken, authorityList, merchant.getMerchantId(),
					privateKey);

			log.info("Successfully retrieved merchant");
			return userDetails;

		} catch (Exception e) {
			throw new AuthenticationCredentialsNotFoundException("Invalid token", e);
		}

	}

	/**
	 * getMerchantPrivateKey
	 * 
	 * @param merchant
	 * @return
	 * @throws Exception
	 */
	private PrivateKey getMerchantPrivateKey(Merchant merchant) throws Exception {
		String privateKeyFilename = PaymentUtils.generateKeyFilename(merchant, PrivateKey.class.getSimpleName());
		String privateKeyPath = keyPrivateFolder + privateKeyFilename;

		PrivateKey privateKey = PaymentUtils.getPrivateKeyFromFile(privateKeyPath);

		return privateKey;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// no additional checks
	}
}
