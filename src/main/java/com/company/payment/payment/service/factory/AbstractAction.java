package com.company.payment.payment.service.factory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.company.payment.payment.model.repository.MerchantRepository;
import com.company.payment.payment.model.repository.TransactionRepository;

public abstract class AbstractAction {

	protected Map<String, String> params;
	protected String uuid;

	@Autowired
	protected TransactionRepository transactionRepository;
	
	@Autowired
	protected MerchantRepository merchantRepository;

	public AbstractAction() {
	}

	public abstract void processRequest();

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
