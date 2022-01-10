package com.company.payment.payment.service.factory;

import org.springframework.security.core.context.SecurityContextHolder;

import com.company.payment.payment.config.login.PaymentUserDetails;
import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.TransactionStatus;
import com.company.payment.payment.model.TransactionType;

public class ChargeAction extends AbstractAction {

	public ChargeAction() {
	}

	@Override
	public void processRequest() throws Exception {
		PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer merchantId = userDetails.getMerchantId();

		String uuid = params.get("uuid");
		Transaction transaction = transactionRepository.getTransactionByUuid(uuid, merchantId,
				TransactionType.AUTHORIZE);

		// save charge transaction
		Transaction chargeTransaction = transaction.clone();
		chargeTransaction.setStatus(TransactionStatus.APPROVED);
		chargeTransaction.setType(TransactionType.CHARGE);
		transactionRepository.saveOrUpdate(chargeTransaction);

		// add merchant amount
		Merchant merchant = merchantRepository.getMerchantById(merchantId);
		Double totalTransactionSum = merchant.getTotalTransactionSum();
		totalTransactionSum += chargeTransaction.getAmount();
		merchant.setTotalTransactionSum(totalTransactionSum);
		merchantRepository.saveOrUpdate(merchant);

		setUuid(transaction.getUuid());
	}
}
