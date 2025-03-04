package com.capo.autenticacion.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.capo.autenticacion.interfaces.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Service
public class JwtServiceImpl implements JwtService {

	@Autowired
	private Environment environment; 
	
	@Override
	public String generateJwt(String subject) {
		return Jwts.builder().subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(Date.from(Instant.now().plus(1,ChronoUnit.HOURS)))
				.signWith(getSigningKey())
				.compact();
	}
	
	//colocar este metoddo en los otros micros que requieran usar JWT from request
	@Override
	public Mono<Boolean> validateJwt(String token) {
		return Mono.just(token)
				.map(this::parseToken)
				.map(claims->!claims.getExpiration().before(new Date()))
				.onErrorReturn(false);
	}
	
	@Override
	public String extractTokenSubject(String token) {
		return parseToken(token).getSubject();
	}
	
	private Claims parseToken(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	private SecretKey getSigningKey() {
		return Optional.ofNullable(environment.getProperty("token.secret"))
				.map(tokenSecret-> tokenSecret.getBytes())
				.map(tokenSecretBytes-> Keys.hmacShaKeyFor(tokenSecretBytes))
				.orElseThrow(()-> new IllegalArgumentException("token.secret must be in application.properties"));
	}
}
