package com.company.payment.payment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MerchantServiceTest {

	@Autowired
	private MerchantService merchantService;

	@Test
	public void testGenerateKeys() throws Exception {
		
		merchantService.generateMerchantKeys(1);
	}
}
