package com.perigea.tracker.calendar.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.perigea.tracker.calendar.enums.CalendarEventType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingEventDto {
	
	private String ID;
	private CalendarEventType type;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date endDate;
	private ContactDto eventCreator;
	
	private List<ContactDto> participants;
	private boolean meetingRoom;
	private boolean inPerson;
	private String descritpion;
	private String link;
}
