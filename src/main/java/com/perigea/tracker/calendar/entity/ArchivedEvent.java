package com.perigea.tracker.calendar.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document (collection = "archived_event")
public class ArchivedEvent {
	@Field
	@Id
	private String id;
	
	@Field
	private ScheduledEvent inactiveEvent;
	

}
