package com.capo.autenticacion.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.capo.autenticacion.entity.UserEntity;
import com.capo.autenticacion.interfaces.AuthenticationService;
import com.capo.autenticacion.interfaces.JwtService;
import com.capo.autenticacion.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class AuthenticationImpl implements AuthenticationService {
	
	@Autowired
	private ReactiveAuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public Mono<Map<String, String>> authentication(String username, String password) {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password))
				.then(getUserDetails(username))
				.map(this::createAuthResponse);
	}
	
	private Mono<UserEntity> getUserDetails(String username){
		return userRepository.findByEmail(username);
	}
	
	private Map<String,String> createAuthResponse(UserEntity user){
		Map<String,String> result= new HashMap<>();
		result.put("userId", user.getId().toString());
		result.put("token", jwtService.generateJwt(user.getId().toString()));
		return result;
	}
}
