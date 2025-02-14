package com.capo.autenticacion.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.capo.autenticacion.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Integer> {
	Mono<UserEntity> findByEmail(String username);
}
