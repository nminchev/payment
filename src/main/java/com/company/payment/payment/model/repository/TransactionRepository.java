package com.company.payment.payment.model.repository;

import java.util.List;

import org.hibernate.query.Query;
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

	/**
	 * retrieve transaction by ReferenceId and merchantId
	 * 
	 * @param referenceId
	 * @param merchantId
	 * @return
	 */
	@Transactional
	public Transaction getTransactionByReferenceId(String referenceId, Integer merchantId) {
		Query<Transaction> query = transactionDao.getCurrentSession()
				.createQuery("FROM " + Transaction.class.getTypeName() + " WHERE reference_id = :reference_id "
						+ " AND merchant_id = :merchant_id", Transaction.class);
		query.setParameter("reference_id", referenceId);
		query.setParameter("merchant_id", merchantId);

		List<Transaction> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}
}
