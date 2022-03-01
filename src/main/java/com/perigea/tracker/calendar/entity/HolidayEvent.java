package com.perigea.tracker.calendar.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.perigea.tracker.commons.annotations.NotNull;
import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.CalendarEventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Document(collection = "event_holiday")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class HolidayEvent extends CalendarEvent {
	
	private static final long serialVersionUID = 6657896728352189565L;

	@NotNull
	@Field
	private Contact responsabile;
	
	@NotNull
	@Field
	private ApprovalStatus approved;
	
	public HolidayEvent (CalendarEventType type) {
		this.setType(type);
		this.setApproved(ApprovalStatus.PENDING);
	}

}
