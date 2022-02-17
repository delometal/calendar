package com.perigea.tracker.calendar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.perigea.tracker.commons.model.Email;

@Component
public class NotificationRestClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${notificator.endpoint}")
	private String endpoint;
	
	public void send(Email email) {
		restTemplate.postForObject(endpoint, email, String.class);
	}
}
