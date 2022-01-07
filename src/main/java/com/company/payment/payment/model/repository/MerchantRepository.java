package com.company.payment.payment.model.repository;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.dao.HibernateDao;

@Repository
public class MerchantRepository {

	@Autowired
	private HibernateDao<Merchant> merchantDao;

	/**
	 * Retrieve Merchant by merchant_id
	 * 
	 * @param merchantId
	 * @return
	 */
	@Transactional
	public Merchant retrieveMerchantById(Integer merchantId) {
		return merchantDao.get(merchantId, Merchant.class);
	}

	/**
	 * Retrieve merchant by email
	 * 
	 * @param email
	 */
	@Transactional
	public Merchant retrieveMerchantByEmail(String email) {
		Query<Merchant> query = merchantDao.getCurrentSession()
				.createQuery("from " + Merchant.class.getTypeName() + " where email = :email", Merchant.class);
		query.setParameter("email", email);
		List<Merchant> list = query.list();

		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Save or update merchant
	 * 
	 * @param merchant
	 */
	@Transactional
	public void saveOrUpdate(Merchant merchant) {
		merchantDao.saveOrUpdate(merchant);
	}
}
