package com.perigea.tracker.calendar.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.perigea.tracker.calendar.dto.ContactDto;
import com.perigea.tracker.calendar.enums.ApprovalStatus;
import com.perigea.tracker.calendar.enums.CalendarEventType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "leave_event")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeaveEvent extends CalendarEvent {
	
	@Field
	private ContactDto superior;
	
	@Field
	private ApprovalStatus approved;
	
	public LeaveEvent (CalendarEventType type) {
		this.setType(type);
		this.setApproved(ApprovalStatus.Pending);
	}

}
