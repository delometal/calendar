package com.perigea.tracker.calendar.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.perigea.tracker.commons.dto.EventContactDto;
import com.perigea.tracker.commons.enums.CalendarEventType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document (collection = "meeting_event")
@EqualsAndHashCode(callSuper=false)
public class MeetingEvent extends CalendarEvent {

	@Field
	private List<EventContactDto> participants;
	
	@Field 
	private boolean meetingRoom;
	
	@Field 
	private boolean inPerson;
	
	@Field 
	private String description;
	
	@Field 
	private String link;
	
	public MeetingEvent() {
		this.setType(CalendarEventType.Riunione);
	}

}
