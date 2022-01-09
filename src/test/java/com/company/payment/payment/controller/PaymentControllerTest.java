package com.company.payment.payment.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PaymentControllerTest {

	private static Logger log = LogManager.getLogger(PaymentControllerTest.class);
	public static final String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJockBlbWFyY2hhbnRwYXkuY29tOzIxIn0.IZMlo2DmTVhtQgk6VADjPs8mZT4Fdnc7CikvTaxOkJDOWVZ_1N2EUWYzGxte0rC0Rf24NVQ13CJIHd1PvQ4v1g";

	@Test
	public void testController() throws Exception {
		TestRestTemplate restTemplate = new TestRestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Bearer " + JWT_TOKEN);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("payload", "encriptedPayload");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		/*
		 * ResponseEntity<String> responseGet =
		 * restTemplate.getForEntity("http://localhost:8080/api/transaction",
		 * String.class, request); HttpStatus statusCodeGet =
		 * responseGet.getStatusCode(); log.info(statusCodeGet);
		 * 
		 * String bodyGet = responseGet.getBody(); log.info(bodyGet);
		 */

		ResponseEntity<String> responsePost = restTemplate.postForEntity("http://localhost:8080/api/transaction",
				request, String.class);
		HttpStatus statusCode = responsePost.getStatusCode();
		log.info(statusCode);

		String body = responsePost.getBody();
		log.info(body);

	}
}
