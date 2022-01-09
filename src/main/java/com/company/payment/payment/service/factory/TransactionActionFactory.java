package com.company.payment.payment.service.factory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class TransactionActionFactory {

	@Autowired
	private AuthorizeAction authorizeAction;

	@Autowired
	private ChargeAction chargeAction;

	@Autowired
	private RefundAction refundAction;

	@Autowired
	private ReversalAction reversalAction;

	public AbstractAction getAction(Map<String, String> params) throws Exception {

		String type = params.get("type");

		switch (type) {
		case "AUTHORIZE": {
			authorizeAction.setParams(params);
			return authorizeAction;
		}
		case "CHARGE": {
			chargeAction.setParams(params);
			return chargeAction;
		}
		case "REFUND": {
			refundAction.setParams(params);
			return refundAction;
		}
		case "REVERSAL": {
			reversalAction.setParams(params);
			return reversalAction;
		}

		default:
			throw new Exception("Invalid transaction type");
		}
	}
}
