package com.perigea.tracker.calendar.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.TimesheetEvent;
import com.perigea.tracker.calendar.entity.TimesheetReferences;
import com.perigea.tracker.calendar.mapper.TimesheetEventMapper;
import com.perigea.tracker.calendar.mapper.TimesheetRefMapper;
import com.perigea.tracker.calendar.service.EventEmailBuilderService;
import com.perigea.tracker.calendar.service.TimesheetEventService;
import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.dto.TimesheetEventDto;
import com.perigea.tracker.commons.dto.TimesheetRefDto;
import com.perigea.tracker.commons.model.Email;

@RestController
@RequestMapping(path = "/timesheet")
@CrossOrigin(allowedHeaders = "*", origins = "*", originPatterns = "*", exposedHeaders = "*")
public class TimesheetEventController {

	@Autowired
	private TimesheetEventService timesheetEventService;

	@Autowired
	private TimesheetEventMapper timesheetMapper;

	@Autowired
	private EventEmailBuilderService emailBuilder;

	@Autowired
	private NotificationRestClient notificator;
	
	@Autowired
	private TimesheetRefMapper refMapper;

	@PostMapping(path = "/create")
	public ResponseEntity<ResponseDto<TimesheetEventDto>> createTimesheetEvent(
			@RequestBody TimesheetEventDto timesheetEvent) {
		TimesheetEvent event = timesheetMapper.mapToEntity(timesheetEvent);
		Email email = emailBuilder.build(timesheetEvent, "creato");
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
		Email email = emailBuilder.build(timesheetEvent, "eliminato");
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
		Email email = emailBuilder.build(timesheetEvent, "modificato");
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
		Email email = emailBuilder.buildApproval(timesheetEvent);
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<TimesheetEventDto>builder().data(timesheetEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("Stato di approvazione: %s", timesheetEvent.getApprovalStatus())).build(),
				HttpStatus.OK);
	}
	
	//FIXME da fixare con le aggregation di mongo
	@GetMapping(value = "/get-last-by-refs/{anno}/{mese}/{codicePersona}")
	public ResponseEntity<ResponseDto<TimesheetEventDto>> findAllByReferences(@PathVariable Integer anno,
			@PathVariable Integer mese, @PathVariable String codicePersona) {
		TimesheetReferences refs = refMapper.mapToEntity(new TimesheetRefDto(codicePersona, anno, mese));
		List<TimesheetEvent> timesheetEvent = timesheetEventService.findByTimesheetReferences(refs);
		List<TimesheetEventDto> eventsDto = timesheetMapper.mapToDtoList(timesheetEvent);
		eventsDto.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
		TimesheetEventDto eventDto = eventsDto.get(eventsDto.size()-1);
		return new ResponseEntity<>(
				ResponseDto.<TimesheetEventDto>builder().data(eventDto).code(HttpStatus.OK.value())
						.description(String.format("evento timesheet dell'utente %s", codicePersona)).build(),
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
