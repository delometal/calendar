package com.perigea.tracker.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MeetingEventException extends GenericException {

	private static final long serialVersionUID = 1664471807917256261L;

	public MeetingEventException(String exception) {
		super(exception);
	}
}