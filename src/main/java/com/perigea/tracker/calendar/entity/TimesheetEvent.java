package com.perigea.tracker.calendar.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.CalendarEventType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "event_holiday")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TimesheetEvent extends CalendarEvent {

	private static final long serialVersionUID = 2301911030689064506L;

	@Field
	private TimesheetReferences timesheet;

	@Field
	private Contact responsabile;

	@Field
	private ApprovalStatus approvalStatus;

	public TimesheetEvent(CalendarEventType type) {
		this.setType(type);
		this.setApprovalStatus(ApprovalStatus.PENDING);
	}
}
