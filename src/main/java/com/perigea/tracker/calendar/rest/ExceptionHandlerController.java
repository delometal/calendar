package com.perigea.tracker.calendar.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.LeaveEventException;
import com.perigea.tracker.commons.exception.MeetingEventException;
import com.perigea.tracker.commons.exception.NotificationSchedulerException;
import com.perigea.tracker.commons.exception.ParticipantException;

@ControllerAdvice
public class ExceptionHandlerController {
	
	@ExceptionHandler(LeaveEventException.class)
	public final ResponseEntity<Response<String>> handleLeaveEventNonFoundException(LeaveEventException ex){
		return new ResponseEntity<>(
				Response.<String>builder()
				.body(null)
				.type(Response.Type.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MeetingEventException.class)
	public final ResponseEntity<Response<String>> handleEntityNonFoundException(MeetingEventException ex){
		return new ResponseEntity<>(
				Response.<String>builder()
				.body(null)
				.type(Response.Type.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ParticipantException.class)
	public final ResponseEntity<Response<String>> handleEntityNonFoundException(ParticipantException ex){
		return new ResponseEntity<>(
				Response.<String>builder()
				.body(null)
				.type(Response.Type.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<Response<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
		return new ResponseEntity<>(
				Response.<String>builder()
				.body(null)
				.type(Response.Type.ERROR)
				.code(HttpStatus.NOT_FOUND.value())
				.description(ex.getMessage())
				.build(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NotificationSchedulerException.class)
	public final ResponseEntity<Response<String>> handleSchedulerException(NotificationSchedulerException ex) {
		return new ResponseEntity<>(
				Response.<String>builder()
				.body(null)
				.type(Response.Type.ERROR)
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.description(ex.getMessage())
				.build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
