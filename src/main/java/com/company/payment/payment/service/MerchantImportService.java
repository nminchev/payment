package com.company.payment.payment.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.company.payment.payment.model.Merchant;

@Service
public class MerchantImportService {

	private static Logger log = LogManager.getLogger(MerchantImportService.class);

	@Value("${payment.merchant.import}")
	private Boolean merchantImport;

	@Value("${payment.merchant.file}")
	private String merchantImportFile;

	@Value("${payment.merchant.delimiter}")
	private String delimiter;

	@Autowired
	private MerchantService merchantService;

	/**
	 * import merchant from CSV is merchantImport is set to true
	 * 
	 * @throws IOException
	 */
	public void importFromFile() throws Exception {
		// try {
		if (merchantImport) {
			processMerchantImport();
		}
		/*
		 * } catch (Exception e) {
		 * log.warn(String.format("Warning during merchant import: %s",
		 * e.getMessage())); }
		 */
	}

	/**
	 * Process merchants import from CSV
	 * 
	 * @throws Exception
	 */
	private void processMerchantImport() throws Exception {
		File merchantImport = ResourceUtils.getFile(merchantImportFile);
		if (!merchantImport.exists()) {
			throw new FileNotFoundException(
					String.format("CSV file %s does not exist on filepath", merchantImportFile));
		}

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(merchantImport))) {

			bufferedReader.readLine();

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(delimiter);

				Merchant merchant = fillMerchantData(values);

				merchantService.createOrUpdateMerchant(merchant);

				log.info(String.format("Successully imported merchant %s", merchant.getEmail()));

			}
		}
	}

	/**
	 * Fill one line for merchant data
	 * 
	 * @param values
	 * @return
	 */
	private Merchant fillMerchantData(String[] values) {
		Merchant merchant = new Merchant();
		merchant.setName(values[0]);
		merchant.setDescription(values[1]);
		merchant.setEmail(values[2]);
		merchant.setStatus(Integer.valueOf(values[3]));
		merchant.setTotalTransactionSum(0.0);
		return merchant;
	}
}
