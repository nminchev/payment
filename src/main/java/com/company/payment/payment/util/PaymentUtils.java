package com.company.payment.payment.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Cipher;

import org.apache.commons.codec.digest.DigestUtils;

import com.company.payment.payment.model.Merchant;

public class PaymentUtils {

	/**
	 * Generate key filename
	 * 
	 * @param merchant
	 * @param type
	 * @return
	 */
	public static String generateKeyFilename(Merchant merchant, String type) {
		StringBuilder filename = new StringBuilder();
		filename.append(type);
		filename.append(merchant.getEmail());
		filename.append(merchant.getMerchantId());

		String encode = DigestUtils.sha256Hex(filename.toString());

		encode = encode.replaceAll("/", "1");

		return encode;
	}

	/**
	 * Utility for encrypt
	 * 
	 * @param plainText
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

		return Base64.getEncoder().encodeToString(cipherText);
	}

	/**
	 * Utility for decrypt
	 * 
	 * @param cipherText
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(cipherText);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), "UTF-8");
	}

	/**
	 * Convert Map to String
	 * 
	 * @param map
	 * @return
	 */
	public static String convertMapToString(Map<?, ?> map) {
		return (String) map.keySet().stream().map(key -> key + "=" + map.get(key)).collect(Collectors.joining(","));
	}

	/**
	 * Convert String to Map
	 * 
	 * @param string
	 * @return
	 */
	public static Map<String, String> convertStringToMap(String string) {
		Map<String, String> map = Arrays.stream(string.split(",")).map(entry -> entry.split("="))
				.collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
		return map;
	}

	/**
	 * reads a public key from a file
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyFromFile(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(spec);
	}

	/**
	 * reads a private key from a file
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromFile(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(spec);
	}

	/**
	 * reads a JWT token from a file
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String getJwtTokenFromFile(String filename) throws Exception {
		String jwtToken = Files.readString(Paths.get(filename));

		return jwtToken;
	}
}
