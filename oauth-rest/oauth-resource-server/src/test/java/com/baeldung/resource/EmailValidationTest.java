package com.baeldung.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.baeldung.resource.ResourceServerApp;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ResourceServerApp.class })
public class EmailValidationTest {

	private final static String AUTH_SERVER = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect";
	private final static String RESOURCE_SERVER = "http://localhost:8081/resource-server";
	private final static String CLIENT_ID = "newClient";
	private final static String CLIENT_ID2 = "newClient2";
	private final static String CLIENT_SECRET = "newClientSecret";
	private final static String USERNAME1 = "john@test.com";
	private final static String USERNAME2 = "john@other.com";
	private final static String PASSWORD = "123";

	@Test
	public void check_username1() {
		String accessToken = obtainAccessToken(CLIENT_ID, USERNAME1, PASSWORD);
		
		Response fooResponse = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.get(RESOURCE_SERVER + "/user/info");
		assertEquals(200, fooResponse.getStatusCode());

	}
	
	@Test
	public void check_username2() {
		String accessToken = obtainAccessToken(CLIENT_ID2, USERNAME2, PASSWORD);
		
		Response fooResponse = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.get(RESOURCE_SERVER + "/user/info");
		assertEquals(401, fooResponse.getStatusCode());
	}

	private String obtainAccessToken(String clientId, String username, String password) {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "password");
		params.put("client_id", clientId);
		params.put("username", username);
		params.put("password", password);
		params.put("scope", "read write");
		final Response response = RestAssured.given().auth().preemptive().basic(clientId, CLIENT_SECRET).and()
				.with().params(params).when().post(AUTH_SERVER + "/token");
		return response.jsonPath().getString("access_token");
	}


}