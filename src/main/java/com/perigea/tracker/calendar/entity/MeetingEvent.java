package com.perigea.tracker.calendar.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.perigea.tracker.commons.annotations.NotNull;
import com.perigea.tracker.commons.enums.CalendarEventType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document (collection = "event_meeting")
@EqualsAndHashCode(callSuper=false)
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
	
	public MeetingEvent() {
		this.setType(CalendarEventType.Riunione);
	}

}
