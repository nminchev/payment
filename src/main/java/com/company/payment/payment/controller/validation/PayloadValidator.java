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
import com.company.payment.payment.model.TransactionType;
import com.company.payment.payment.util.PaymentUtils;

public class PayloadValidator implements ConstraintValidator<PayloadValid, String> {

	private static Logger log = LogManager.getLogger(PayloadValidator.class);

	@Autowired
	private EmailValidator emailValidator;

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
			validateAmount(params, context);
			validateCustomerEmail(params, context);
			validateCustomerPhone(params, context);
			validateUuid(params, context);
			validateReferenceId(params, context);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
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
	 * @param context
	 */
	private void validateReferenceId(Map<String, String> params, ConstraintValidatorContext context)
			throws ValidationException {
		if (params.containsKey("reference_id")) {
			String referenceId = params.get("reference_id");
			if (referenceId == null || "".equals(referenceId)) {
				throw new ValidationException("referenceId is empty");
			}
		}
	}

	/**
	 * validate uuid
	 * 
	 * @param params
	 * @param context
	 */
	private void validateUuid(Map<String, String> params, ConstraintValidatorContext context)
			throws ValidationException {
		if (!params.containsKey("uuid")) {
			throw new ValidationException("uuid is not present");
		}

		String uuid = params.get("uuid");
		if (uuid == null || "".equals(uuid)) {
			throw new ValidationException("uuid is empty");
		}
	}

	/**
	 * validate customer_phone
	 * 
	 * @param params
	 * @param context
	 */
	private void validateCustomerPhone(Map<String, String> params, ConstraintValidatorContext context)
			throws ValidationException {
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
	 * @param context
	 */
	private void validateCustomerEmail(Map<String, String> params, ConstraintValidatorContext context)
			throws ValidationException {
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
	 * @param context
	 * @throws ValidationException
	 */
	private void validateAmount(Map<String, String> params, ConstraintValidatorContext context)
			throws ValidationException {
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
