package com.capo.autenticacion.interfaces;

import reactor.core.publisher.Mono;

//subject is userId in this app
public interface JwtService {
	String generateJwt(String subject);
	Mono<Boolean> validateJwt(String token);
	String extractTokenSubject(String token);
}
