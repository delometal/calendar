package com.perigea.tracker.calendar.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.perigea.tracker.commons.enums.EventStatus;
import com.perigea.tracker.commons.model.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document (collection = "scheduled_event")
public class ScheduledEvent implements Serializable{
	
	private static final long serialVersionUID = -1079361041861609552L;

	@Field
	@Id
	private String id;
	
	@Field
	private Email email;
	
	@Field
	private Date nextFireTime;
	
	@Field 
	private Date expiration;
	
	@Field
	private EventStatus status;
	
	@Field
	private String tipo;
	
	@Field
	private String cron;
}

