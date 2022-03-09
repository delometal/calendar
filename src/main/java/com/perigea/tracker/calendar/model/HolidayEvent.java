package com.perigea.tracker.calendar.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.HolidayType;
import com.perigea.tracker.commons.utils.Utils;

import lombok.Data;

@Data
public class HolidayEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2387245176091190252L;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATE_FORMAT, timezone = "Europe/Rome")
	private LocalDateTime data;
	private ApprovalStatus status;
	private HolidayType tipo;
	private Integer ore;

	public HolidayEvent() {
		this.status = ApprovalStatus.PENDING;
	}

}
