package com.company.payment.payment.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api")
public class PaymentController {

	private static Logger log = LogManager.getLogger(PaymentController.class);

	@RequestMapping(value = "transaction", method = RequestMethod.POST)
	public String postTransaction(@RequestParam(required = true) String payload) {
		log.info("payload=" + payload);

		return "found";
	}

}
