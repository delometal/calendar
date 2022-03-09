package com.perigea.tracker.calendar.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.HolidayType;

import lombok.Data;

@Data
public class HolidayEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2387245176091190252L;
	
	private LocalDateTime data;
	private ApprovalStatus status;
	private HolidayType tipo;
	private Integer ore;

	public HolidayEvent() {
		this.status = ApprovalStatus.PENDING;
	}

}
