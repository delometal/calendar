package com.perigea.tracker.calendar.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.perigea.tracker.commons.annotations.NotNull;
import com.perigea.tracker.commons.enums.CalendarEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CalendarEvent implements Serializable {
	
	private static final long serialVersionUID = -2787298694404603816L;

	@Id
	private String id;
	
	@NotNull
	@Field
	private CalendarEventType type;
	
	// Conoscenza CC email
	@Field
	private List<Contact> conoscenzaCC;
	
	@NotNull
	@Field
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Rome")
	private Date startDate;
	
	@NotNull
	@Field
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Rome")
	private Date endDate;
	
	@NotNull
	@Field 
	private Contact eventCreator;
	
}
