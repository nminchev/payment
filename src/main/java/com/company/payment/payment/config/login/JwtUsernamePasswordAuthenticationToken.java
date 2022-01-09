package com.company.payment.payment.config.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -56048420363389529L;

	private Integer merchantId;

	public JwtUsernamePasswordAuthenticationToken(String email, String jwtToken, Integer merchantId) {
		super(email, jwtToken);
		this.merchantId = merchantId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

}
