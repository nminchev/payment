package com.company.payment.payment;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@SpringBootTest
class PaymentApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testKeyGenerations() throws Exception {
		KeyPairGenerator generator = null;
		generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);

		KeyPair pair = generator.generateKeyPair();

		byte[] privateKey = pair.getPrivate().getEncoded();
		byte[] publicKey = pair.getPublic().getEncoded();

		String jwtToken = JWT.create().withSubject("email").sign(Algorithm.HMAC512(privateKey));

		String user = JWT.require(Algorithm.HMAC512(privateKey)).build().verify(jwtToken).getSubject();

		String jwtToken2 = JWT.create().withSubject("email").sign(Algorithm.HMAC512(privateKey));

		String user2 = JWT.require(Algorithm.HMAC512(privateKey)).build().verify(jwtToken).getSubject();

	}

}
