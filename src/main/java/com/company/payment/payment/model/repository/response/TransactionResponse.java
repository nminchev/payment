package com.company.payment.payment.model.repository.response;

public class TransactionResponse {

	private String uuid;

	private String error;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
