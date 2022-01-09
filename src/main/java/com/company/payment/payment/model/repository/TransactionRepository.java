package com.company.payment.payment.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.dao.HibernateDao;

@Repository
public class TransactionRepository {

	@Autowired
	private HibernateDao<Transaction> transactionDao;

	/**
	 * Save or update merchant
	 * 
	 * @param merchant
	 */
	@Transactional
	public void saveOrUpdate(Transaction transaction) {
		transactionDao.saveOrUpdate(transaction);
	}
}
