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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.company.payment.payment.model.Merchant;
import com.company.payment.payment.model.repository.MerchantRepository;
import com.company.payment.payment.util.PaymentConstants;
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
		Merchant merchant = merchantRepository.getMerchantById(merchantId);

		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);

		KeyPair pair = generator.generateKeyPair();

		PrivateKey privateKey = pair.getPrivate();
		savePrivateKeyToFile(merchant, privateKey);

		PublicKey publicKey = pair.getPublic();
		savePublicKeyToFile(merchant, publicKey);

		String jwtToken = generateJwtToken(merchant);
		saveJwtTokenToFile(merchant, jwtToken);

	}

	/**
	 * Saves private key to file system
	 * 
	 * @param merchant
	 * @param jwtToken
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveJwtTokenToFile(Merchant merchant, String jwtToken) throws FileNotFoundException, IOException {
		String publicKeyFilename = merchant.getEmail() + "_jwtToken";
		String publicKeyPath = keyPublicFolder + publicKeyFilename;
		File publicKeyFile = new File(publicKeyPath);
		publicKeyFile.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
			fos.write(jwtToken.getBytes());
		}

	}

	/**
	 * Saves public key to file system
	 * 
	 * @param merchant
	 * @param publicKey
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void savePublicKeyToFile(Merchant merchant, PublicKey publicKey) throws IOException, FileNotFoundException {
		String publicKeyFilename = merchant.getEmail() + "_PublicKey";
		String publicKeyPath = keyPublicFolder + publicKeyFilename;
		File publicKeyFile = new File(publicKeyPath);
		publicKeyFile.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
			fos.write(publicKey.getEncoded());
		}
	}

	/**
	 * Generate JWT token based on merchant email and merchant id
	 * 
	 * @param merchant
	 * @return
	 */
	private String generateJwtToken(Merchant merchant) {
		String subject = merchant.getEmail() + PaymentConstants.TOKEN_SEPARATOR + merchant.getMerchantId();
		String jwtToken = JWT.create().withSubject(subject).sign(Algorithm.HMAC512(keyCommon.getBytes()));
		return jwtToken;
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
	private void savePrivateKeyToFile(Merchant merchant, PrivateKey privateKey)
			throws IOException, FileNotFoundException {
		String privateKeyFilename = PaymentUtils.generateKeyFilename(merchant, PrivateKey.class.getSimpleName());
		String privateKeyPath = keyPrivateFolder + privateKeyFilename;
		File privateKeyFile = new File(privateKeyPath);
		privateKeyFile.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
			fos.write(privateKey.getEncoded());
		}
	}

	/**
	 * Create or update existing Merchant
	 * 
	 * @param merchant
	 * @throws Exception
	 */
	public void createOrUpdateMerchant(Merchant newMerchant) throws Exception {
		String email = newMerchant.getEmail();

		Merchant merchant = merchantRepository.getMerchantByEmail(email);
		if (merchant == null) {
			merchantRepository.saveOrUpdate(newMerchant);

			generateMerchantKeys(newMerchant.getMerchantId());
		} else {
			merchant.setName(newMerchant.getName());
			merchant.setDescription(newMerchant.getDescription());
			merchant.setStatus(newMerchant.getStatus());

			merchantRepository.saveOrUpdate(merchant);
		}
	}
}
