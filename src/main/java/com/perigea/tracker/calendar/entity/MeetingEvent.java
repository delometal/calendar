package com.perigea.tracker.calendar.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.perigea.tracker.calendar.model.Contact;
import com.perigea.tracker.commons.annotations.NotNull;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.utils.Utils;

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
	
	@NotNull
	@Field
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATE_FORMAT, timezone = "Europe/Rome")
	private LocalDateTime startDate;
	
	@NotNull
	@Field
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATE_FORMAT, timezone = "Europe/Rome")
	private LocalDateTime endDate;
	
	public MeetingEvent() {
		this.setType(CalendarEventType.RIUNIONE);
	}

}
