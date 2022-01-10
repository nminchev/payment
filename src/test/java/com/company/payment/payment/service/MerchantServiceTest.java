package com.company.payment.payment.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.repository.MerchantRepository;

@SpringBootTest
public class MerchantServiceTest {

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private MerchantRepository merchantRepository;

	@Test
	@Rollback
	public void testGenerateKeys() throws Exception {
		Merchant merchant = merchantRepository.getMerchantByEmail("hr@emarchantpay.com");

		merchantService.generateMerchantKeys(merchant.getMerchantId());
		
		assertNotNull(merchant);
	}
}
