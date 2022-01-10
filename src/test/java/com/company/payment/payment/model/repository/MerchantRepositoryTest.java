package com.company.payment.payment.model.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.company.payment.payment.model.Merchant;

@SpringBootTest
public class MerchantRepositoryTest {

	@Autowired
	private MerchantRepository merchantRepository;

	@Test
	public void testRetriveeMerchantById() throws Exception {
		Merchant merchant = merchantRepository.getMerchantById(1);

		assertNotNull(merchant);
	}
}
