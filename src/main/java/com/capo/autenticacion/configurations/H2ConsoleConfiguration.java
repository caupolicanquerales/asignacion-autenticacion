package com.capo.autenticacion.configurations;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@Profile("!prod & !production")
public class H2ConsoleConfiguration {
	
	private final String WEB_PORT="8082";
	private Server webServer;
	
	@EventListener(ApplicationStartedEvent.class)
	public void start() throws SQLException{
		this.webServer= Server.createWebServer("-webPort",WEB_PORT).start();
	}
	
	@EventListener(ContextClosedEvent.class)
	public void stop() {
		this.webServer.stop();
	}
}
