package com.perigea.tracker.calendar.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.calendar.mapper.LeaveMapper;
import com.perigea.tracker.calendar.service.EmailBuilderService;
import com.perigea.tracker.calendar.service.LeaveEventService;
import com.perigea.tracker.commons.dto.LeaveEventDto;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.model.Email;

@RestController
@RequestMapping(path = "/leave")
public class LeaveEventController {

	@Autowired
	private LeaveEventService leaveService;

	@Autowired
	private LeaveMapper leaveMapper;

	@Autowired
	private EmailBuilderService emailBuilder;
	
	@Autowired
	private NotificationRestClient notificator;
	
	@PostMapping(path = "/add-Leave-Event")
	public ResponseEntity<Response<LeaveEventDto>> addLeaveEvent(@RequestBody LeaveEventDto leaveEvent) {
		LeaveEvent event = leaveMapper.mapToEntity(leaveEvent);
		Email email = emailBuilder.buildFromLeaveEvent(event, "creato");
		notificator.mandaNotifica(email);
		leaveService.save(event);
		return new ResponseEntity<>(
				Response.<LeaveEventDto>builder().body(leaveEvent).code(HttpStatus.OK.value())
						.description(String.format("%s %s salvato", leaveEvent.getType(),leaveEvent.getID())).build(),
				HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/delete-Event")
	public ResponseEntity<Response<LeaveEventDto>> deleteLeaveEvent(@RequestBody LeaveEventDto leaveEvent) {
		LeaveEvent toBeDeleted = leaveMapper.mapToEntity(leaveEvent);
		leaveService.delete(toBeDeleted);
		Email email = emailBuilder.buildFromLeaveEvent(toBeDeleted, "eliminato");
		notificator.mandaNotifica(email);
		return new ResponseEntity<>(Response.<LeaveEventDto>builder().body(leaveEvent).code(HttpStatus.OK.value())
				.description(String.format("%s eliminato", leaveEvent.getType())).build(), HttpStatus.OK);
	}

	@PutMapping(path = "/update-event")
	public ResponseEntity<Response<LeaveEventDto>> updateLeaveEvent(@RequestBody LeaveEventDto leaveEvent) {
		LeaveEvent toBeUpdated = leaveMapper.mapToEntity(leaveEvent);
		leaveService.update(toBeUpdated);
		Email email = emailBuilder.buildFromLeaveEvent(toBeUpdated, "modificato");
		notificator.mandaNotifica(email);
		return new ResponseEntity<>(Response.<LeaveEventDto>builder().body(leaveEvent).code(HttpStatus.OK.value())
				.description(String.format("%s %s aggiornato", leaveEvent.getType(),leaveEvent.getID())).build(), 
				HttpStatus.OK);
		
	}
	
	@GetMapping(path = "/get-By-Date-Creator-Type", params = { "mailAziendaleCeator", "from", "to", "type" })
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByCreatorBetweenDates(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@RequestParam String mailAziendaleCeator, @RequestParam CalendarEventType type) {
		List<LeaveEvent> events = leaveService.findAllByDateCreatorType(from, to, mailAziendaleCeator, type);
		List<LeaveEventDto> leaves = leaveMapper.mapToDtoList(events);
		return new ResponseEntity<>(Response.<List<LeaveEventDto>>builder().body(leaves).code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi %s di %s dal %s al %s", mailAziendaleCeator)).build(),
				HttpStatus.OK);
	}

	@GetMapping(path = "/get-By-Date-responsabile-Type", params = { "mailAziendaleResopnsabile", "from", "to",
			"type" })
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByResponsabileBewtweenDates(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@RequestParam String mailAziendaleResponsabile, @RequestParam CalendarEventType type) {
		List<LeaveEvent> events = leaveService.findAllByDateResponsabileType(from, to, mailAziendaleResponsabile, type);
		List<LeaveEventDto> leaves = leaveMapper.mapToDtoList(events);
		return new ResponseEntity<>(Response.<List<LeaveEventDto>>builder().body(leaves).code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi %s di %s dal %s al %s", mailAziendaleResponsabile))
				.build(), HttpStatus.OK);
	}

	@GetMapping(path = "/get-By-Event-Creator", params = { "mailAziendaleCreator" })
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByCreator(@RequestParam String mailAziendaleCreator) {
		List<LeaveEvent> events = leaveService.findAllByEventCreator(mailAziendaleCreator);
		List<LeaveEventDto> leaves = leaveMapper.mapToDtoList(events);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder().body(leaves).code(HttpStatus.OK.value())
						.description(String.format("Lista dei permessi di %s", mailAziendaleCreator)).build(),
				HttpStatus.OK);
	}

	@GetMapping(path = "/get-By-Responsabile", params = { "mailAziendaleResponsabile" })
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByResponsabile(
			@RequestParam String mailAziendaleResponsabile) {
		List<LeaveEvent> events = leaveService.findAllByResponsabile(mailAziendaleResponsabile);
		List<LeaveEventDto> leaves = leaveMapper.mapToDtoList(events);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder().body(leaves).code(HttpStatus.OK.value())
						.description(String.format("Lista dei permessi di %s", mailAziendaleResponsabile)).build(),
				HttpStatus.OK);
	}

}
