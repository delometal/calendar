package com.perigea.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParticipantException extends GenericException {

	private static final long serialVersionUID = 1664471807917256261L;

	public ParticipantException(String exception) {
		super(exception);
	}
}