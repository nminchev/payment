package com.company.payment.payment.model.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.company.payment.payment.model.Transaction;

@SpringBootTest
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	@Rollback
	public void testGetOldTransactions() throws Exception {
		List<Transaction> oldTransactions = transactionRepository.getOldTransactions();

		assertNotNull(oldTransactions);
	}
}
