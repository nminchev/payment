package com.company.payment.payment.controller.validation;

import java.security.PrivateKey;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.company.payment.payment.config.login.PaymentUserDetails;
import com.company.payment.payment.model.Transaction;
import com.company.payment.payment.model.TransactionType;
import com.company.payment.payment.model.repository.MerchantRepository;
import com.company.payment.payment.model.repository.TransactionRepository;
import com.company.payment.payment.util.PaymentUtils;

public class PayloadValidator implements ConstraintValidator<PayloadValid, String> {

	private static Logger log = LogManager.getLogger(PayloadValidator.class);

	@Autowired
	private EmailValidator emailValidator;

	@Autowired
	protected TransactionRepository transactionRepository;

	@Autowired
	protected MerchantRepository merchantRepository;

	@Override
	public boolean isValid(String payload, ConstraintValidatorContext context) {
		try {
			PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			PrivateKey privateKey = userDetails.getPrivateKey();

			String decrypt = PaymentUtils.decrypt(payload, privateKey);
			log.info("decrypt=" + decrypt);

			Map<String, String> params = PaymentUtils.convertStringToMap(decrypt);
			validateType(params, context);
			String type = params.get("type");
			TransactionType transactionType = TransactionType.valueOf(type);

			validateAmount(params, transactionType, context);
			validateCustomerEmail(params, transactionType, context);
			validateCustomerPhone(params, transactionType, context);
			validateReferenceId(params, transactionType, context);
			validateUuid(params, transactionType, context);

		} catch (Exception e) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();

			log.error(e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Validate type
	 * 
	 * @param params
	 * @param context
	 */
	private void validateType(Map<String, String> params, ConstraintValidatorContext context)
			throws ValidationException {
		if (!params.containsKey("type")) {
			throw new ValidationException("type is not present");
		}

		String type = params.get("type");
		if (type == null || "".equals(type)) {
			throw new ValidationException("type is empty");
		}

		TransactionType.valueOf(type);

	}

	/**
	 * validate reference_id
	 * 
	 * @param params
	 * @param transactionType
	 * @param context
	 */
	private void validateReferenceId(Map<String, String> params, TransactionType transactionType,
			ConstraintValidatorContext context) throws ValidationException {
		if (!params.containsKey("reference_id")) {
			throw new ValidationException("reference_id is not present");
		}

		String referenceId = params.get("reference_id");
		if (referenceId == null || "".equals(referenceId)) {
			throw new ValidationException("referenceId is empty");
		}

		if (transactionType == TransactionType.AUTHORIZE) {
			PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Integer merchantId = userDetails.getMerchantId();

			Transaction transaction = transactionRepository.getTransactionByReferenceId(referenceId, merchantId,
					TransactionType.AUTHORIZE);
			if (transaction != null) {
				throw new ValidationException(
						String.format("transaction with reference_id %s already authorized", referenceId));
			}
		}
	}

	/**
	 * validate uuid
	 * 
	 * @param params
	 * @param transactionType
	 * @param context
	 */
	private void validateUuid(Map<String, String> params, TransactionType transactionType,
			ConstraintValidatorContext context) throws ValidationException {
		PaymentUserDetails userDetails = (PaymentUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Integer merchantId = userDetails.getMerchantId();

		// uuid should be present in case of CHARGE and REFUND
		if (transactionType == TransactionType.CHARGE || transactionType == TransactionType.REFUND
				|| transactionType == TransactionType.REVERSAL) {
			if (!params.containsKey("uuid")) {
				throw new ValidationException("uuid is not present");
			}

			String uuid = params.get("uuid");
			if (uuid == null || "".equals(uuid)) {
				throw new ValidationException("uuid is empty");
			}

			if (transactionType == TransactionType.CHARGE) {
				Transaction changeTransaction = transactionRepository.getTransactionByUuid(uuid, merchantId,
						TransactionType.CHARGE);
				if (changeTransaction != null) {
					throw new ValidationException(String.format("transaction with uuid %s already charged", uuid));
				}
			}

			// check for valid uuid in DB for the merchant
			if (transactionType == TransactionType.CHARGE || transactionType == TransactionType.REFUND
					|| transactionType == TransactionType.REVERSAL) {

				Transaction authorizeTransaction = transactionRepository.getTransactionByUuid(uuid, merchantId,
						TransactionType.AUTHORIZE);
				if (authorizeTransaction == null) {
					throw new ValidationException(String.format("transaction with uuid %s not authorized", uuid));
				}

				Double amount = Double.valueOf(params.get("amount"));
				if (!authorizeTransaction.getAmount().equals(amount)) {
					throw new ValidationException("transaction amount does not match");
				}

				String customerPhone = params.get("customer_phone");
				if (!authorizeTransaction.getCustomerPhone().equals(customerPhone)) {
					throw new ValidationException("transaction customer_phone does not match");
				}

				String customerEmail = params.get("customer_email");
				if (!authorizeTransaction.getCustomerEmail().equals(customerEmail)) {
					throw new ValidationException("transaction customer_email does not match");
				}

				String referenceId = params.get("reference_id");
				if (!authorizeTransaction.getReferenceId().equals(referenceId)) {
					throw new ValidationException("transaction reference_id does not match");
				}

			}
		}

		// no uuid in case of AUTHORIZE transaction
		if (transactionType == TransactionType.AUTHORIZE) {
			if (params.containsKey("uuid")) {
				throw new ValidationException("uuid should not present");
			}
		}
	}

	/**
	 * validate customer_phone
	 * 
	 * @param params
	 * @param transactionType
	 * @param context
	 */
	private void validateCustomerPhone(Map<String, String> params, TransactionType transactionType,
			ConstraintValidatorContext context) throws ValidationException {
		if (!params.containsKey("customer_phone")) {
			throw new ValidationException("customer_phone is not present");
		}

		String customerPhone = params.get("customer_phone");
		if (customerPhone == null || "".equals(customerPhone)) {
			throw new ValidationException("customer_phone is empty");
		}

	}

	/**
	 * Validate customer email
	 * 
	 * @param params
	 * @param transactionType
	 * @param context
	 */
	private void validateCustomerEmail(Map<String, String> params, TransactionType transactionType,
			ConstraintValidatorContext context) throws ValidationException {
		if (!params.containsKey("customer_email")) {
			throw new ValidationException("customer_email is not present");
		}

		String customerEmail = params.get("customer_email");
		if (customerEmail == null || "".equals(customerEmail)) {
			throw new ValidationException("customer_email is empty");
		}

		boolean valid = emailValidator.isValid(customerEmail, context);
		if (!valid) {
			throw new ValidationException("customer_email is not valid");
		}
	}

	/**
	 * Validate amount
	 * 
	 * @param params
	 * @param transactionType
	 * @param context
	 * @throws ValidationException
	 */
	private void validateAmount(Map<String, String> params, TransactionType transactionType,
			ConstraintValidatorContext context) throws ValidationException {
		if (!params.containsKey("amount")) {
			throw new ValidationException("Amount is not present");
		}

		String amountStr = params.get("amount");
		if (amountStr == null || "".equals(amountStr)) {
			throw new ValidationException("amount is empty");
		}

		Double amount = Double.valueOf(params.get("amount"));
		if (amount <= 0) {
			throw new ValidationException("Amount should be greater than 0");
		}
	}
}
