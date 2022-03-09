package com.perigea.tracker.calendar.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.perigea.tracker.calendar.model.Contact;
import com.perigea.tracker.commons.annotations.NotNull;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.enums.ReminderEventTime;

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
	private Contact eventCreator;

	@Field
	private ReminderEventTime reminederTime;

}
