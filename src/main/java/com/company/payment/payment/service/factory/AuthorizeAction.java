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
	public void processRequest() throws Exception {
		Transaction transaction = new Transaction();

		Double amount = Double.valueOf(params.get("amount"));
		transaction.setAmount(amount);

		String customerEmail = params.get("customer_email");
		transaction.setCustomerEmail(customerEmail);

		String customerPhone = params.get("customer_phone");
		transaction.setCustomerPhone(customerPhone);

		String referenceId = params.get("reference_id");
		transaction.setReferenceId(referenceId);

		Merchant merchant = retrieveMerchant();
		transaction.setMerchant(merchant);

		transaction.setStatus(TransactionStatus.APPROVED);
		transaction.setType(TransactionType.AUTHORIZE);

		transactionRepository.saveOrUpdate(transaction);

		setUuid(transaction.getUuid());
	}

	/**
	 * Retrieve merchant
	 * 
	 * @return
	 */
	private Merchant retrieveMerchant() {
		PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer merchantId = userDetails.getMerchantId();
		Merchant merchant = merchantRepository.getMerchantById(merchantId);
		return merchant;
	}
}
