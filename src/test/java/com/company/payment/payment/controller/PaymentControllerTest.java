package com.company.payment.payment.controller;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PaymentControllerTest {
	private static Logger log = LogManager.getLogger(PaymentControllerTest.class);

	private static final String MERCHANT_EMAIL = "hr@emarchantpay.com";

	private static final String HOST = "http://localhost:8080/api/transaction";

	@Value("${key.public.folder}")
	private String keyPublicFolder;

	private String uuid;
	private String amount = "2000";
	private String customerEmail = "customer@gmail.com";
	private String customerPhone = "0888567856";
	private String referenceId = "10000003"; // should be unique for merchant

	@Test
	@Order(value = 1)
	public void testAuthorizeTran() throws Exception {
		TestRestTemplate restTestTemplate = new TestRestTemplate();

		HttpHeaders headers = prepareHeader();

		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put("type", TransactionType.AUTHORIZE.toString());
		payloadMap.put("amount", amount);
		payloadMap.put("customer_email", customerEmail);
		payloadMap.put("customer_phone", customerPhone);
		payloadMap.put("reference_id", referenceId);

		HttpEntity<PaymentPayload> request = prepareRequest(headers, payloadMap);

		ResponseEntity<TransactionResponse> responsePost = restTestTemplate.postForEntity(HOST, request,
				TransactionResponse.class);
		HttpStatus statusCode = responsePost.getStatusCode();
		log.info(statusCode);

		TransactionResponse body = responsePost.getBody();
		log.info("error=" + body.getError());
		log.info("uuid=" + body.getUuid());

		uuid = body.getUuid();

	}

	@Test
	@Order(value = 2)
	public void testChargeTran() throws Exception {
		TestRestTemplate restTestTemplate = new TestRestTemplate();

		HttpHeaders headers = prepareHeader();

		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put("type", TransactionType.CHARGE.toString());
		payloadMap.put("uuid", uuid);
		payloadMap.put("amount", amount);
		payloadMap.put("customer_email", customerEmail);
		payloadMap.put("customer_phone", customerPhone);
		payloadMap.put("reference_id", referenceId);

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
	@Order(value = 3)
	public void testRefundTran() throws Exception {
		TestRestTemplate restTestTemplate = new TestRestTemplate();

		HttpHeaders headers = prepareHeader();

		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put("type", TransactionType.REFUND.toString());
		payloadMap.put("uuid", uuid);
		payloadMap.put("amount", amount);
		payloadMap.put("customer_email", customerEmail);
		payloadMap.put("customer_phone", customerPhone);
		payloadMap.put("reference_id", referenceId);

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
	@Order(value = 4)
	public void testReversalTran() throws Exception {
		TestRestTemplate restTestTemplate = new TestRestTemplate();

		HttpHeaders headers = prepareHeader();

		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put("type", TransactionType.REVERSAL.toString());
		payloadMap.put("uuid", uuid);
		payloadMap.put("customer_email", customerEmail);
		payloadMap.put("customer_phone", customerPhone);
		payloadMap.put("reference_id", referenceId);

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
