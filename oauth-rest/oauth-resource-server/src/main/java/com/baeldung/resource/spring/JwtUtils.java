package com.baeldung.resource.spring;

import org.springframework.stereotype.Component;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;


@Component
public class JwtUtils {

	public JwtClaims getUserNameFromJwtToken(String token) throws Exception {              
	       JwtConsumer consumer = new JwtConsumerBuilder()
	               .setSkipAllValidators()
	               .setDisableRequireSignature()
	               .setSkipSignatureVerification()
	               .build();
	       JwtClaims claims = consumer.processToClaims(token);  
	       
	       return claims;      
	}
}
