package com.company.payment.payment.service.factory;

import org.springframework.security.core.context.SecurityContextHolder;

import com.company.payment.payment.config.login.PaymentUserDetails;
import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.TransactionStatus;
import com.company.payment.payment.model.TransactionType;

public class RefundAction extends AbstractAction {

	public RefundAction() {
	}

	@Override
	public void processRequest() throws Exception {
		PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer merchantId = userDetails.getMerchantId();

		String uuid = params.get("uuid");
		Transaction chargeTransaction = transactionRepository.getTransactionByUuid(uuid, merchantId,
				TransactionType.CHARGE);

		// add REFUND transaction
		Transaction refundTransaction = chargeTransaction.clone();
		refundTransaction.setStatus(TransactionStatus.APPROVED);
		refundTransaction.setType(TransactionType.REFUND);
		transactionRepository.saveOrUpdate(refundTransaction);

		// subtract merchant amount
		Merchant merchant = merchantRepository.getMerchantById(merchantId);
		Double totalTransactionSum = merchant.getTotalTransactionSum();
		totalTransactionSum -= refundTransaction.getAmount();
		merchant.setTotalTransactionSum(totalTransactionSum);
		merchantRepository.saveOrUpdate(merchant);

		// save charge transaction to REFUNDED
		chargeTransaction.setStatus(TransactionStatus.REFUNDED);
		transactionRepository.saveOrUpdate(refundTransaction);

		setUuid(chargeTransaction.getUuid());
	}
}
