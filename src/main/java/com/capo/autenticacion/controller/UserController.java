package com.capo.autenticacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.capo.autenticacion.interfaces.UserService;
import com.capo.autenticacion.request.UserCreationRequest;
import com.capo.autenticacion.response.UserResponse;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/creation")
	public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody Mono<UserCreationRequest> request) {
		return this.userService.createUser(request).map(ResponseEntity.status(HttpStatus.CREATED)::body)
		.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	/*
	 * Prueba de endpoint con autorizacion
	 * */
	
	@GetMapping("/prueba")
	public Mono<ResponseEntity<String>> pruebaEndpoint(@RequestBody Mono<UserCreationRequest> request) {
		return Mono.just("ok").map(ResponseEntity.status(HttpStatus.OK)::body)
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
}
