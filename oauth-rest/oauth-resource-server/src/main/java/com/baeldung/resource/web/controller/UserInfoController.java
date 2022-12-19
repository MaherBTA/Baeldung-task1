package com.baeldung.resource.web.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

	@GetMapping("/user/info")
	public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt principal, HttpServletResponse response)
			throws IOException {

		if (principal.getClaimAsString("preferred_username").endsWith("@test.com"))

			return Collections.singletonMap("user_name", principal.getClaimAsString("preferred_username"));

		else {
			(
			(HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
			return null;
		}

	}
}