package com.company.payment.payment.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.payment.payment.model.repository.response.TransactionResponse;
import com.company.payment.payment.service.PaymentService;

@RestController
@RequestMapping(path = "api")
@Validated
public class PaymentController {
	private static Logger log = LogManager.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	/**
	 * post transactions
	 * 
	 * @param payload
	 * @param httpServletResponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "transaction", method = RequestMethod.POST)
	@ResponseBody
	public TransactionResponse postTransaction(@Valid @RequestBody PaymentPayload payload,
			HttpServletResponse httpServletResponse) throws Exception {
		log.info("payload=" + payload);

		TransactionResponse response = paymentService.postMerchantTransaction(payload.getPayload());

		return response;
	}

	/**
	 * Handle invalid PaymentPayload exceptions
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	TransactionResponse handleConstraintViolationException(MethodArgumentNotValidException e) {
		TransactionResponse response = new TransactionResponse();
		response.setError(e.getFieldError().toString());
		return response;
	}
}
