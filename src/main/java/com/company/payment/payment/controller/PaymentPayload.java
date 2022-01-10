package com.company.payment.payment.controller;

import com.company.payment.payment.controller.validation.PayloadValid;

public class PaymentPayload {

	@PayloadValid
	private String payload;

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
