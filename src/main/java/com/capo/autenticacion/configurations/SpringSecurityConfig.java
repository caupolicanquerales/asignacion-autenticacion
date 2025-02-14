package com.capo.autenticacion.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.capo.autenticacion.interfaces.JwtService;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {
	
	/*Permite user el authentication Manager creado para esta app*/
	@Bean
	SecurityWebFilterChain httpSecurityFilterChain(ServerHttpSecurity http, 
			ReactiveAuthenticationManager authenticationManager,JwtService jwtService) {
		
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);
		
		return http.authorizeExchange(exchange-> exchange
				.pathMatchers(HttpMethod.POST,"/users/creation").permitAll()
				.pathMatchers(HttpMethod.POST,"/login").permitAll()
				.anyExchange().authenticated())
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.authenticationManager(authenticationManager)
				.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
