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

import com.company.payment.payment.model.TransactionType;
import com.company.payment.payment.model.repository.response.TransactionResponse;
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
		TestRestTemplate restTestTemplate = new TestRestTemplate();

		HttpHeaders headers = prepareHeader();

		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put("type", TransactionType.AUTHORIZE.toString());
		payloadMap.put("amount", "100.11");
		payloadMap.put("customer_email", "customer@gmail.com");
		payloadMap.put("customer_phone", "0888567856");
		payloadMap.put("reference_id", "10000001");

		HttpEntity<PaymentPayload> request = prepareRequest(headers, payloadMap);

		ResponseEntity<TransactionResponse> responsePost = restTestTemplate.postForEntity(HOST, request,
				TransactionResponse.class);
		HttpStatus statusCode = responsePost.getStatusCode();
		log.info(statusCode);

		TransactionResponse body = responsePost.getBody();
		log.info("error=" + body.getError());
		log.info("uuid=" + body.getUuid());

	}

	@Test
	public void testChargeTran() throws Exception {
		TestRestTemplate restTestTemplate = new TestRestTemplate();

		HttpHeaders headers = prepareHeader();

		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put("type", TransactionType.CHARGE.toString());
		payloadMap.put("uuid", "2ad328e2-c967-4996-be75-208596b81b3d");
		payloadMap.put("amount", "100.11");
		payloadMap.put("customer_email", "customer@gmail.com");
		payloadMap.put("customer_phone", "0888567856");
		payloadMap.put("reference_id", "10000001");

		HttpEntity<PaymentPayload> request = prepareRequest(headers, payloadMap);

		ResponseEntity<TransactionResponse> responsePost = restTestTemplate.postForEntity(HOST, request,
				TransactionResponse.class);
		HttpStatus statusCode = responsePost.getStatusCode();
		log.info(statusCode);

		TransactionResponse body = responsePost.getBody();
		log.info("error=" + body.getError());
		log.info("uuid=" + body.getUuid());

	}

	/**
	 * Prepare JSON request
	 * 
	 * @param headers
	 * @param payloadMap
	 * @return
	 * @throws Exception
	 */
	private HttpEntity<PaymentPayload> prepareRequest(HttpHeaders headers, Map<String, String> payloadMap)
			throws Exception {
		String publicKeyPath = keyPublicFolder + MERCHANT_EMAIL + "_PublicKey";
		PublicKey publicKey = PaymentUtils.getPublicKeyFromFile(publicKeyPath);

		String payload = PaymentUtils.convertMapToString(payloadMap);
		payload = PaymentUtils.encrypt(payload, publicKey);

		PaymentPayload paymentPayload = new PaymentPayload();
		paymentPayload.setPayload(payload);

		HttpEntity<PaymentPayload> request = new HttpEntity<PaymentPayload>(paymentPayload, headers);
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
		headers.setContentType(MediaType.APPLICATION_JSON);

		String jwtTokenPath = keyPublicFolder + MERCHANT_EMAIL + "_jwtToken";
		String jwtToken = PaymentUtils.getJwtTokenFromFile(jwtTokenPath);
		headers.add("Authorization", "Bearer " + jwtToken);
		return headers;
	}
}
