package com.perigea.tracker.calendar.entity;

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
public class ScheduledEvent {
	
	public enum Tipo {PERIODICO, ISTANTANEA}
	
	@Field
	@Id
	private String id;
	
	@Field
	private Email email;
	
	@Field
	private Date nextFireTime;
	
	@Field
	private EventStatus status;
	
	@Field
	private String tipo;
	
	@Field
	private String cron;
}

