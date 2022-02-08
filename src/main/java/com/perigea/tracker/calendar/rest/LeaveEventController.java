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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.calendar.mapper.LeaveMapper;
import com.perigea.tracker.calendar.service.LeaveEventService;
import com.perigea.tracker.commons.dto.LeaveEventDto;
import com.perigea.tracker.commons.enums.CalendarEventType;

@RestController
public class LeaveEventController {

	@Autowired
	private LeaveEventService leaveService;
	
	@Autowired
	private LeaveMapper leaveMapper;
	
	@PostMapping(path = "/leave/addLeaveEvent")
	public ResponseEntity<Response<LeaveEvent>> addLeaveEvent(@RequestBody LeaveEvent leaveEvent) {
		leaveService.save(leaveEvent);	
		return new ResponseEntity<>(
				Response.<LeaveEvent>builder()
				.body(leaveEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("Meeting %s inserito nel calendario", leaveEvent.getID()))
				.build(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/leave/getByDateCreatorType", params = {"mailAziendaleCeator", "from", "to", "type"})
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByCreatorBetweenDates(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@RequestParam String mailAziendaleCeator,
			@RequestParam CalendarEventType type) {
		List<LeaveEventDto> events = leaveService.findAllByDateCreatorType(from, to, mailAziendaleCeator, type);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder()
				.body(events)
				.code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi %s di %s dal %s al %s", mailAziendaleCeator))
				.build(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/leave/getByDateCreatorType", params = {"mailAziendaleResopnsabile", "from", "to", "type"})
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByResponsabileBewtweenDates(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@RequestParam String mailAziendaleResponsabile,
			@RequestParam CalendarEventType type) {
		List<LeaveEventDto> events = leaveService.findAllByDateResponsabileType(from, to, mailAziendaleResponsabile, type);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder()
				.body(events)
				.code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi %s di %s dal %s al %s", mailAziendaleResponsabile))
				.build(), HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/leave/getByEventCreator", params = {"mailAziendaleCreator"})
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByCreator(@RequestParam String mailAziendaleCreator) {
		List<LeaveEventDto> events = leaveService.findAllByEventCreator(mailAziendaleCreator);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder()
				.body(events)
				.code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi di %s", mailAziendaleCreator))
				.build(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/leave/getByResponsabile", params = {"mailAziendaleResponsabile"})
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByResponsabile(@RequestParam String mailAziendaleResponsabile) {
		List<LeaveEventDto> events = leaveService.findAllByResponsabile(mailAziendaleResponsabile);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder()
				.body(events)
				.code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi di %s", mailAziendaleResponsabile))
				.build(), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/leave/deleteLeaveEvent")
	public ResponseEntity<Response<LeaveEventDto>> deleteLeaveEvent(@RequestBody LeaveEventDto leaveEvent) {
		leaveService.delete(leaveMapper.mapToEntity(leaveEvent));	
		return new ResponseEntity<>(
				Response.<LeaveEventDto>builder()
				.body(leaveEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("%s eliminato", leaveEvent.getType()))
				.build(), HttpStatus.OK);
	}
}
