package com.example.demo.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerAuthentificationFilter extends UsernamePasswordAuthenticationFilter {
	
	private static final String APPLICATION_JSON_VALUE = "application/json";
	private AuthenticationManager authenticationManager;
	
	

	public CustomerAuthentificationFilter(AuthenticationManager authenticationManager) {
		
		this.authenticationManager = authenticationManager;
	}
	

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenticationToken);
		//return super.attemptAuthentication(request, response);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		
		User user = (User) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		
		String access_token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+24 *60 *60 *1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
		
		String refresh_token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+30 *60 *1000))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);

	    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	    response.setHeader("Access-Control-Allow-Credentials", "true");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		response.setContentType(APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);

		//super.successfulAuthentication(request, response, chain, authResult);
	}

}
