package com.company.payment.payment.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.company.payment.payment.service.PaymentService;

@Component
public class TransactionScheduleJob {
	private static Logger log = LogManager.getLogger(TransactionScheduleJob.class);

	@Autowired
	private PaymentService paymentService;

	@Scheduled(fixedRate = 60000)
	public void reportCurrentTime() {
		log.info("Starting Transaction Schedule Job");

		paymentService.deleteOldTranscations();
	}
}
