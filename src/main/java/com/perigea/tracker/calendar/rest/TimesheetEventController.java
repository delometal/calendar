package com.perigea.tracker.calendar.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.TimesheetEvent;
import com.perigea.tracker.calendar.mapper.TimesheetEventMapper;
import com.perigea.tracker.calendar.service.EventEmailBuilderService;
import com.perigea.tracker.calendar.service.TimesheetEventService;
import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.dto.TimesheetEventDto;
import com.perigea.tracker.commons.model.Email;

@RestController
@RequestMapping(path = "/timesheet")
public class TimesheetEventController {

	@Autowired
	private TimesheetEventService timesheetEventService;

	@Autowired
	private TimesheetEventMapper timesheetMapper;

	@Autowired
	private EventEmailBuilderService emailBuilder;

	@Autowired
	private NotificationRestClient notificator;

	@PostMapping(path = "/create")
	public ResponseEntity<ResponseDto<TimesheetEventDto>> createTimesheetEvent(
			@RequestBody TimesheetEventDto timesheetEvent) {
		TimesheetEvent event = timesheetMapper.mapToEntity(timesheetEvent);
		Email email = emailBuilder.build(event, "creato");
		notificator.send(email);
		timesheetEventService.save(event);
		return new ResponseEntity<>(ResponseDto.<TimesheetEventDto>builder().data(timesheetEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("%s %s salvato", timesheetEvent.getType(), timesheetEvent.getId())).build(),
				HttpStatus.OK);
	}

	@DeleteMapping(path = "/delete")
	public ResponseEntity<ResponseDto<TimesheetEventDto>> deleteTimesheetEvent(
			@RequestBody TimesheetEventDto timesheetEvent) {
		TimesheetEvent toBeDeleted = timesheetMapper.mapToEntity(timesheetEvent);
		timesheetEventService.delete(toBeDeleted);
		Email email = emailBuilder.build(toBeDeleted, "eliminato");
		notificator.send(email);
		return new ResponseEntity<>(
				ResponseDto.<TimesheetEventDto>builder().data(timesheetEvent).code(HttpStatus.OK.value())
						.description(String.format("%s eliminato", timesheetEvent.getType())).build(),
				HttpStatus.OK);
	}

	@PutMapping(path = "/update")
	public ResponseEntity<ResponseDto<TimesheetEventDto>> updateTimesheetEvent(
			@RequestBody TimesheetEventDto timesheetEvent) {
		TimesheetEvent toBeUpdated = timesheetMapper.mapToEntity(timesheetEvent);
		timesheetEventService.update(toBeUpdated);
		Email email = emailBuilder.build(toBeUpdated, "modificato");
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<TimesheetEventDto>builder().data(timesheetEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("%s %s aggiornato", timesheetEvent.getType(), timesheetEvent.getId()))
				.build(), HttpStatus.OK);
	}

	@PutMapping(path = "/approve")
	public ResponseEntity<ResponseDto<TimesheetEventDto>> approveEvent(@RequestBody TimesheetEventDto timesheetEvent) {
		TimesheetEvent toBeApproved = timesheetMapper.mapToEntity(timesheetEvent);
		timesheetEventService.update(toBeApproved);
		Email email = emailBuilder.buildApproval(toBeApproved);
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<TimesheetEventDto>builder().data(timesheetEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("Stato di approvazione: %s", timesheetEvent.getApprovalStatus())).build(),
				HttpStatus.OK);
	}

	@GetMapping(path = "/get-by-event-creator/{mailAziendaleCreator}")
	public ResponseEntity<ResponseDto<List<TimesheetEventDto>>> findAllByCreator(
			@PathVariable String mailAziendaleCreator) {
		List<TimesheetEvent> events = timesheetEventService.findAllByEventCreator(mailAziendaleCreator);
		List<TimesheetEventDto> holidays = timesheetMapper.mapToDtoList(events);
		return new ResponseEntity<>(
				ResponseDto.<List<TimesheetEventDto>>builder().data(holidays).code(HttpStatus.OK.value())
						.description(String.format("Lista dei permessi di %s", mailAziendaleCreator)).build(),
				HttpStatus.OK);
	}
}
