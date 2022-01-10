package com.company.payment.payment.model.repository;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.TransactionStatus;
import com.company.payment.payment.model.TransactionType;
import com.company.payment.payment.model.dao.HibernateDao;

@Repository
public class TransactionRepository {

	@Value("${transaction.delete.interval}")
	private Integer transactionDeleteInterval;

	@Autowired
	private HibernateDao<Transaction> transactionDao;

	/**
	 * Save or update transaction
	 * 
	 * @param transaction
	 */
	@Transactional
	public void saveOrUpdate(Transaction transaction) {
		transactionDao.saveOrUpdate(transaction);
	}

	/**
	 * Delete transaction
	 * 
	 * @param transaction
	 */
	@Transactional
	public void delete(Transaction transaction) {
		transactionDao.delete(transaction);
	}

	/**
	 * retrieve transaction by ReferenceId and merchantId
	 * 
	 * @param referenceId
	 * @param merchantId
	 * @param type
	 * @return
	 */
	@Transactional
	public Transaction getTransactionByReferenceId(String referenceId, Integer merchantId, TransactionType type) {
		Query<Transaction> query = transactionDao.getCurrentSession().createQuery(
				"FROM " + Transaction.class.getTypeName() + " WHERE reference_id = :reference_id "
						+ " AND merchant_id = :merchant_id " + " AND type = :type " + " AND status = :status",
				Transaction.class);
		query.setParameter("reference_id", referenceId);
		query.setParameter("merchant_id", merchantId);
		query.setParameter("status", TransactionStatus.APPROVED);
		query.setParameter("type", type);

		List<Transaction> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	/**
	 * retrieve transaction by uuid and merchantId
	 * 
	 * @param uuid
	 * @param merchantId
	 * @return
	 */
	@Transactional
	public Transaction getTransactionByUuid(String uuid, Integer merchantId, TransactionType type) {
		Query<Transaction> query = transactionDao.getCurrentSession()
				.createQuery(
						"FROM " + Transaction.class.getTypeName() + " WHERE uuid = :uuid "
								+ " AND merchant_id = :merchant_id" + " AND type = :type " + " AND status = :status",
						Transaction.class);
		query.setParameter("uuid", uuid);
		query.setParameter("merchant_id", merchantId);
		query.setParameter("status", TransactionStatus.APPROVED);
		query.setParameter("type", type);

		List<Transaction> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Delete old transactions
	 * @return
	 */
	@Transactional
	public List<Transaction> getOldTransactions() {
		Query<Transaction> query = transactionDao.getCurrentSession().createQuery(
				"FROM " + Transaction.class.getTypeName() + " WHERE hour(sysdate() - create_date) >= :interval",
				Transaction.class);
		query.setParameter("interval", transactionDeleteInterval);
		return query.list();
	}
}
