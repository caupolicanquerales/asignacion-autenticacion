package com.capo.autenticacion.interfaces;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import com.capo.autenticacion.request.UserCreationRequest;
import com.capo.autenticacion.response.UserResponse;

import reactor.core.publisher.Mono;

public interface UserService extends ReactiveUserDetailsService{
	Mono<UserResponse> createUser(Mono<UserCreationRequest> userCreation);
}
