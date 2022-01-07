package com.company.payment.payment;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.company.payment.payment.service.MerchantImportService;

@SpringBootApplication
public class PaymentApplication {
	private static Logger log = LogManager.getLogger(PaymentApplication.class);

	@Autowired
	private MerchantImportService merchantImportService;

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

	@PostConstruct
	public void start() throws Exception {
		log.info("Importing Merchants from CSV");
		merchantImportService.importFromFile();
	}

}
