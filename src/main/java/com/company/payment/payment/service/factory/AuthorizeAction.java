package com.company.payment.payment.service.factory;

import org.springframework.security.core.context.SecurityContextHolder;

import com.company.payment.payment.config.login.PaymentUserDetails;
import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.TransactionStatus;
import com.company.payment.payment.model.TransactionType;

public class AuthorizeAction extends AbstractAction {

	public AuthorizeAction() {
	}

	@Override
	public void processRequest() {
		Double amount = Double.valueOf(params.get("amount"));
		String customerEmail = params.get("customer_email");
		String customerPhone = params.get("customer_phone");

		PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Integer merchantId = userDetails.getMerchantId();

		Merchant merchant = merchantRepository.retrieveMerchantById(merchantId);

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setCustomerEmail(customerEmail);
		transaction.setCustomerPhone(customerPhone);
		transaction.setMerchant(merchant);
		transaction.setStatus(TransactionStatus.APPROVED);
		transaction.setType(TransactionType.AUTHORIZE);

		transactionRepository.saveOrUpdate(transaction);

		setUuid(transaction.getUuid());

	}

}
