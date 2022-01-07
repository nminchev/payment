package com.company.payment.payment.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
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
public class TransactionTest {

	@Autowired
	private HibernateDao<Transaction> transactionDao;

	@Autowired
	private HibernateDao<Merchant> merchantDao;

	@Test
	//@Commit
	@Rollback
	public void testTransactionInsert() {

		Query<Merchant> query = merchantDao.getCurrentSession()
				.createQuery("from " + Merchant.class.getTypeName() + " where email = :email", Merchant.class);
		query.setParameter("email", "hr@emarchantpay.com");
		List<Merchant> list = query.list();

		if (list.size() > 0) {

			Transaction transaction = new Transaction();
			transaction.setAmount(10.12);
			transaction.setCustomerEmail("niko@email.com");
			transaction.setCustomerPhone("0888567856");
			transaction.setStatus(TransactionStatus.APPROVED);
			transaction.setType(TransactionType.AUTHORIZE);
			transaction.setMerchant(list.get(0));

			transactionDao.save(transaction);

			assertNotNull(transaction.getTransactionId());
		}

	}
}
