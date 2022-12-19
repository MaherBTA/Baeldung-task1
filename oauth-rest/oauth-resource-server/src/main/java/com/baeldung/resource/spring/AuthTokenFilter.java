package com.baeldung.resource.spring;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = parseJwt(request);
		try {
			JwtClaims claim = jwtUtils.getUserNameFromJwtToken(jwt);
			String username = (String) claim.getClaimsMap().get("preferred_username");

			if (username.endsWith("@test.com"))
				filterChain.doFilter(request, response);
			else
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
						"The token is not valid.");

		} catch (Exception e) {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
			e.printStackTrace();
		}

	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}