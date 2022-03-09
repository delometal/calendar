package com.perigea.tracker.calendar.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.perigea.tracker.calendar.model.Contact;
import com.perigea.tracker.commons.annotations.NotNull;
import com.perigea.tracker.commons.enums.CalendarEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@Document(collection = "event_meeting")
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
public class MeetingEvent extends CalendarEvent {

	private static final long serialVersionUID = 6217270905717232791L;

	@NotNull
	@Field
	private List<Contact> participants;

	@Field
	private Boolean meetingRoom;

	@NotNull
	@Field
	private Boolean inPerson;

	@NotNull
	@Field
	private String description;

	@Field
	private String link;
	
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
	
	public MeetingEvent() {
		this.setType(CalendarEventType.Riunione);
	}

}
