package com.company.payment.payment.controller;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.company.payment.payment.model.TransactionType;
import com.company.payment.payment.util.PaymentUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PaymentControllerTest {
	private static Logger log = LogManager.getLogger(PaymentControllerTest.class);

	private static final String MERCHANT_EMAIL = "hr@emarchantpay.com";

	private static final String HOST = "http://localhost:8080/api/transaction";

	@Value("${key.public.folder}")
	private String keyPublicFolder;

	@Test
	public void testAuthorizeTran() throws Exception {
		TestRestTemplate restTemplate = new TestRestTemplate();

		HttpHeaders headers = prepareHeader();

		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put("type", TransactionType.AUTHORIZE.toString());
		payloadMap.put("amount", "100.11");
		payloadMap.put("customer_email", "customer@gmail.com");
		payloadMap.put("customer_phone", "0888567856");
		payloadMap.put("reference_id", "ddd");

		HttpEntity<MultiValueMap<String, String>> request = prepareRequest(headers, payloadMap);

		ResponseEntity<String> responsePost = restTemplate.postForEntity(HOST, request, String.class);
		HttpStatus statusCode = responsePost.getStatusCode();
		log.info(statusCode);

		String body = responsePost.getBody();
		log.info(body);

	}

	/**
	 * Prepare Request with Public Key encryption
	 * 
	 * @param headers
	 * @param payloadMap
	 * @return
	 * @throws Exception
	 */
	private HttpEntity<MultiValueMap<String, String>> prepareRequest(HttpHeaders headers,
			Map<String, String> payloadMap) throws Exception {
		String publicKeyPath = keyPublicFolder + MERCHANT_EMAIL + "_PublicKey";
		PublicKey publicKey = PaymentUtils.getPublicKeyFromFile(publicKeyPath);

		String payload = PaymentUtils.convertMapToString(payloadMap);
		payload = PaymentUtils.encrypt(payload, publicKey);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("payload", payload);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		return request;
	}

	/**
	 * Prepare Header with JWT Token
	 * 
	 * @return
	 * @throws Exception
	 */
	private HttpHeaders prepareHeader() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String jwtTokenPath = keyPublicFolder + MERCHANT_EMAIL + "_jwtToken";
		String jwtToken = PaymentUtils.getJwtTokenFromFile(jwtTokenPath);
		headers.add("Authorization", "Bearer " + jwtToken);
		return headers;
	}
}
