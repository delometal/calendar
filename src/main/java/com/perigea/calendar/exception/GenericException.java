package com.perigea.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GenericException extends RuntimeException {
	private static final long serialVersionUID = 4093329276438371635L;

	public GenericException(String exception) {
		super(exception);
	}
}
