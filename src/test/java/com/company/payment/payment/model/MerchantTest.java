package com.company.payment.payment.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.company.payment.payment.model.dao.HibernateDao;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class MerchantTest {

	@Autowired
	private HibernateDao<Merchant> merchantDao;

	@Test
	// @Commit
	@Rollback
	public void testMerchantInsert() {

		Merchant merchant = new Merchant();
		merchant.setName("emerchantpay");
		merchant.setDescription("emerchantpay company in Bulgaria");
		merchant.setEmail("hr@emerchantpay.com");
		merchant.setStatus(1);
		merchant.setTotalTransactionSum(10.123);

		merchantDao.save(merchant);

		assertNotNull(merchant.getMerchantId());

	}

	@Test
	// @Commit
	@Rollback
	public void testMerchantUpdate() {

		List<Merchant> allMerchants = merchantDao.findAll(Merchant.class);
		assertNotEquals(0, allMerchants.size());

		Merchant merchant = allMerchants.get(0);
		merchant.setName("New Merchant Name");
		merchant.setDescription("New Merchant Description");

		merchantDao.save(merchant);

		int merchantId = merchant.getMerchantId();

		Merchant merchantUpdated = merchantDao.get(merchantId, Merchant.class);

		assertNotNull(merchantUpdated);

		assertEquals("New Merchant Name", merchantUpdated.getName());
		assertEquals("New Merchant Description", merchantUpdated.getDescription());

	}
}
