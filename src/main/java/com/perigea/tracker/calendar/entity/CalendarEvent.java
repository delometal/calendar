package com.perigea.tracker.calendar.entity;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.perigea.tracker.commons.dto.EventContactDto;
import com.perigea.tracker.commons.enums.CalendarEventType;

import lombok.Data;

@Data
@Document
public abstract class CalendarEvent {
	
	@Id
	private String ID;
	
	@Field
	private CalendarEventType type;
	
	@Field
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Rome")
	private Date startDate;
	
	@Field
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Rome")
	private Date endDate;
	
	// cfr codicePersona timetracker/entity/AnagraficaDipendente
	@Field 
	private EventContactDto eventCreator;
	
}
