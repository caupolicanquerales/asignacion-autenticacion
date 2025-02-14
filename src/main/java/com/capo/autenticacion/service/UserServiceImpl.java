package com.capo.autenticacion.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capo.autenticacion.entity.UserEntity;
import com.capo.autenticacion.interfaces.UserService;
import com.capo.autenticacion.repository.UserRepository;
import com.capo.autenticacion.request.UserCreationRequest;
import com.capo.autenticacion.response.UserResponse;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder; 
	
	@Override
	public Mono<UserResponse> createUser(Mono<UserCreationRequest> userCreation) {
		return userCreation.flatMap(this::getUserEntity)
				.flatMap(userRepository::save).mapNotNull(this::getUserResponse);
	}
	
	/*Este estructura hace el calculo del password non-blocking*/
	private Mono<UserEntity> getUserEntity(UserCreationRequest userCreation) {
		return Mono.fromCallable(()->{
			UserEntity userEntity= new UserEntity();
			userEntity.setEmail(userCreation.getEmail());
			userEntity.setFirtsName(userCreation.getFirstName());
			userEntity.setLastName(userCreation.getLastName());
			userEntity.setPassword(passwordEncoder.encode(userCreation.getPassword()));
			return userEntity;
		}).subscribeOn(Schedulers.boundedElastic());
		
	}
	
	private UserResponse getUserResponse(UserEntity userEntity) {
		UserResponse userResponse= new UserResponse();
		userResponse.setEmail(userEntity.getEmail());
		userResponse.setFirtsName(userEntity.getFirtsName());
		userResponse.setLastName(userEntity.getLastName());
		userResponse.setId(userEntity.getId());
		return userResponse;
	}

	/* Pasa el password y email al modelo userDetail*/
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByEmail(username).map(userEntity->
		User.withUsername(userEntity.getEmail())
		.password(userEntity.getPassword())
		.authorities(new ArrayList<>())
		.build());
	}
}
