package com.company.payment.payment.config.login;

import java.security.PrivateKey;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class PaymentUserDetails extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 759399566501185367L;

	private Integer merchantId;

	private PrivateKey privateKey;

	public PaymentUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
			Integer merchantId, PrivateKey privateKey) {
		super(username, password, authorities);
		this.merchantId = merchantId;
		this.privateKey = privateKey;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

}
