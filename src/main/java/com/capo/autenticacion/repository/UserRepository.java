package com.capo.autenticacion.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.capo.autenticacion.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
	Mono<UserEntity> findByEmail(String username);
}
