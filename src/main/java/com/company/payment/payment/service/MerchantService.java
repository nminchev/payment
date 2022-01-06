package com.company.payment.payment.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.repository.MerchantRepository;
import com.company.payment.payment.util.PaymentUtils;

@Service
public class MerchantService {

	@Value("${key.common}")
	private String keyCommon;

	@Value("${key.public.folder}")
	private String keyPublicFolder;

	@Value("${key.private.folder}")
	private String keyPrivateFolder;

	@Autowired
	private MerchantRepository merchantRepository;

	/**
	 * Generate Merchant keys
	 * 
	 * @param merchantId
	 * @throws Exception
	 */
	public void generateMerchantKeys(Integer merchantId) throws Exception {
		Merchant merchant = merchantRepository.retriveeMerchantById(merchantId);

		KeyPairGenerator generator = null;
		generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);

		KeyPair pair = generator.generateKeyPair();

		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();

		saveKeysToFileSystem(merchant, privateKey, publicKey);
	}

	/**
	 * Saves key to file system
	 * 
	 * @param merchant
	 * @param privateKey
	 * @param publicKey
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void saveKeysToFileSystem(Merchant merchant, PrivateKey privateKey, PublicKey publicKey)
			throws IOException, FileNotFoundException {
		String publicKeyFilename = PaymentUtils.generateKeyFilename(merchant, PublicKey.class.getSimpleName());
		String publicKeyPath = keyPublicFolder + publicKeyFilename;
		File publicKeyFile = new File(publicKeyPath);
		publicKeyFile.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
			fos.write(publicKey.getEncoded());
		}

		String privateKeyFilename = PaymentUtils.generateKeyFilename(merchant, PrivateKey.class.getSimpleName());
		String privateKeyPath = keyPrivateFolder + privateKeyFilename;
		File privateKeyFile = new File(privateKeyPath);
		privateKeyFile.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
			fos.write(privateKey.getEncoded());
		}
	}

}
