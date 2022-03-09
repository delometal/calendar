package com.perigea.tracker.calendar.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@PropertySource("classpath:/application.properties")
public class ApplicationProperties {
	
	@Value("${notificator.endpoint}")
	private String notificatorEndpoint;
	
	@Value("${password.endpoint}")
	private String passwordUpdateEndpoint;
	
	@Value("${email.sender}")
	private String emailSender;

}
