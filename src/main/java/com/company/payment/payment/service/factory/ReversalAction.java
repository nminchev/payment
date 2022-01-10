package com.company.payment.payment.service.factory;

import org.springframework.security.core.context.SecurityContextHolder;

import com.company.payment.payment.config.login.PaymentUserDetails;
import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.TransactionStatus;
import com.company.payment.payment.model.TransactionType;

public class ReversalAction extends AbstractAction {

	public ReversalAction() {
	}

	@Override
	public void processRequest() throws Exception {
		PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer merchantId = userDetails.getMerchantId();

		String uuid = params.get("uuid");
		Transaction authorizeTransaction = transactionRepository.getTransactionByUuid(uuid, merchantId,
				TransactionType.AUTHORIZE);

		// save REVERSAL transaction
		Transaction reversalTransaction = authorizeTransaction.clone();
		reversalTransaction.setStatus(TransactionStatus.APPROVED);
		reversalTransaction.setType(TransactionType.REVERSAL);
		transactionRepository.saveOrUpdate(reversalTransaction);

		// save AUTHORIZE transaction
		authorizeTransaction.setStatus(TransactionStatus.REVERSED);
		transactionRepository.saveOrUpdate(authorizeTransaction);

		setUuid(authorizeTransaction.getUuid());
	}

}
