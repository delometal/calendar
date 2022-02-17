package com.perigea.tracker.calendar.entity;

import java.io.Serializable;

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
public class ArchivedEvent implements Serializable {
	
	private static final long serialVersionUID = 6113426555386150661L;

	@Field
	@Id
	private String id;
	
	@Field
	private ScheduledEvent inactiveEvent;
	
}
