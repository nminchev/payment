package com.company.payment.payment.util;

import org.apache.commons.codec.digest.DigestUtils;

import com.company.payment.payment.model.Merchant;

public class PaymentUtils {

	/**
	 * Generate key filename
	 * 
	 * @param merchant
	 * @param passwordEncoder
	 * @return
	 */
	public static String generateKeyFilename(Merchant merchant, String type) {
		StringBuilder filename = new StringBuilder();
		filename.append(type);
		filename.append(merchant.getEmail());
		filename.append(merchant.getMerchantId());

		String encode = DigestUtils.sha256Hex(filename.toString());

		encode = encode.replaceAll("/", "1");

		return encode;
	}
}
