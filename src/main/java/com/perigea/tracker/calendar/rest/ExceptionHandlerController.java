package com.perigea.tracker.calendar.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.enums.ResponseType;
import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.HolidayEventException;
import com.perigea.tracker.commons.exception.MeetingEventException;
import com.perigea.tracker.commons.exception.MeetingRoomReservedException;
import com.perigea.tracker.commons.exception.NotificationSchedulerException;
import com.perigea.tracker.commons.exception.NullFieldException;
import com.perigea.tracker.commons.exception.ParticipantException;
import com.perigea.tracker.commons.exception.TimesheetEventException;

@ControllerAdvice
public class ExceptionHandlerController {
	
	@ExceptionHandler(HolidayEventException.class)
	public final ResponseEntity<ResponseDto<String>> handleLeaveEventNonFoundException(HolidayEventException ex){
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TimesheetEventException.class)
	public final ResponseEntity<ResponseDto<String>> handleEntityNotFoundException(TimesheetEventException ex) {
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MeetingEventException.class)
	public final ResponseEntity<ResponseDto<String>> handleEntityNonFoundException(MeetingEventException ex){
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NullFieldException.class)
	public final ResponseEntity<ResponseDto<String>> fieldMustNotBeNullException(NullFieldException ex){
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ParticipantException.class)
	public final ResponseEntity<ResponseDto<String>> handleEntityNonFoundException(ParticipantException ex){
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ResponseDto<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.NOT_FOUND.value())
				.description(ex.getMessage())
				.build(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NotificationSchedulerException.class)
	public final ResponseEntity<ResponseDto<String>> handleSchedulerException(NotificationSchedulerException ex) {
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.description(ex.getMessage())
				.build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MeetingRoomReservedException.class)
	public final ResponseEntity<ResponseDto<String>> handleMeetingEventReservedException(MeetingRoomReservedException ex){
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(null)
				.type(ResponseType.ERROR)
				.code(HttpStatus.BAD_REQUEST.value())
				.description(ex.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}

}
