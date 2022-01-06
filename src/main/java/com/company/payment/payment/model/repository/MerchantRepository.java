package com.company.payment.payment.model.repository;

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
	public Merchant retriveeMerchantById(Integer merchantId) {
		return merchantDao.get(merchantId, Merchant.class);
	}

}
