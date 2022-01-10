package com.company.payment.payment.service;

import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.company.payment.payment.config.login.PaymentUserDetails;
import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.repository.TransactionRepository;
import com.company.payment.payment.model.repository.response.TransactionResponse;
import com.company.payment.payment.service.factory.AbstractAction;
import com.company.payment.payment.service.factory.TransactionActionFactory;
import com.company.payment.payment.util.PaymentUtils;

@Service
public class PaymentService {

	private static Logger log = LogManager.getLogger(PaymentService.class);

	@Autowired
	private TransactionActionFactory transactionActionFactory;

	@Autowired
	protected TransactionRepository transactionRepository;

	public TransactionResponse postMerchantTransaction(String payload) {
		TransactionResponse response = new TransactionResponse();

		try {
			Map<String, String> params = getPayloadParams(payload);

			AbstractAction action = transactionActionFactory.getAction(params);
			action.processRequest();

			response.setUuid(action.getUuid());

		} catch (Exception e) {
			response.setError("Error processing request");
			log.info(e.getMessage());
		}

		return response;

	}

	/**
	 * Get decrypted payload params
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getPayloadParams(String payload) throws Exception {
		PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		PrivateKey privateKey = userDetails.getPrivateKey();

		String decrypt = PaymentUtils.decrypt(payload, privateKey);
		log.info("decrypt=" + decrypt);

		Map<String, String> params = PaymentUtils.convertStringToMap(decrypt);
		return params;
	}

	/**
	 * Delete transaction older than specified interval
	 */
	public void deleteOldTranscations() {
		List<Transaction> transactions = transactionRepository.getOldTransactions();
		if (transactions.size() > 0) {
			log.info(String.format("Deleting %s transactions ", transactions.size()));
			for (Transaction transaction : transactions) {
				transactionRepository.delete(transaction);
			}
		}
	}
}
