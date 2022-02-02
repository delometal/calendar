package com.perigea.calendar.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.perigea.calendar.enums.ApprovalStatus;
import com.perigea.calendar.enums.CalendarEventType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeaveEventDto {
	
	private String ID;
	private CalendarEventType type;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date endDate;
	private ContactDto eventCreator;
	
	private ContactDto superior;
	private ApprovalStatus approved;
	
}
