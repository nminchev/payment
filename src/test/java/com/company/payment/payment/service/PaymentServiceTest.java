package com.company.payment.payment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class PaymentServiceTest {

	@Autowired
	private PaymentService paymentService;

	@Test
	@Rollback
	public void testDeleteOldTranscations() throws Exception {
		paymentService.deleteOldTranscations();
	}
}
