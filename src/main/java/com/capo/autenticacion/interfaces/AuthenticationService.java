package com.capo.autenticacion.interfaces;

import java.util.Map;

import reactor.core.publisher.Mono;

public interface AuthenticationService {
	Mono<Map<String,String>> authentication(String username, String password);
}
