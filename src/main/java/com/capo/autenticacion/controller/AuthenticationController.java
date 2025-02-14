package com.capo.autenticacion.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capo.autenticacion.interfaces.AuthenticationService;
import com.capo.autenticacion.request.AuthenticationRequest;

import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService; 
	
	private final String wordInHeader= "Bearer ";
	
	@PostMapping("/login")
	public Mono<ResponseEntity<Object>> login(@RequestBody Mono<AuthenticationRequest> request) {
		return request.flatMap(req->authenticationService.authentication(req.getEmail(), req.getPassword()))
				.map(auth->ResponseEntity.ok()
						.header(HttpHeaders.AUTHORIZATION,wordInHeader+auth.get("token"))
						.header("UserId", auth.get("userId"))
						.build())
						.onErrorReturn(BadCredentialsException.class,
								ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"))
						.onErrorReturn(Exception.class,ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
}
