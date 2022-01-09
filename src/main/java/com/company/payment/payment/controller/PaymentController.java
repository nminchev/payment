package com.company.payment.payment.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.payment.payment.model.repository.response.TransactionResponse;
import com.company.payment.payment.service.PaymentService;

@RestController
@RequestMapping(path = "api")
public class PaymentController {

	private static Logger log = LogManager.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = "transaction", method = RequestMethod.POST)
	public TransactionResponse postTransaction(@RequestParam(required = true) String payload) throws Exception {
		log.info("payload=" + payload);

		TransactionResponse response = paymentService.postMerchantTransaction(payload);

		return response;
	}

}
