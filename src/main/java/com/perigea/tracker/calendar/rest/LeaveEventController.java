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

import com.perigea.tracker.calendar.dto.ContactDto;
import com.perigea.tracker.calendar.dto.LeaveEventDto;
import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.calendar.enums.CalendarEventType;
import com.perigea.tracker.calendar.mapper.LeaveMapper;
import com.perigea.tracker.calendar.service.LeaveEventService;

@RestController
public class LeaveEventController {

	@Autowired
	private LeaveEventService leaveService;
	
	@Autowired
	private LeaveMapper leaveMapper;
	
	@PostMapping(path = "/leave")
	public ResponseEntity<Response<LeaveEvent>> addLeaveEvent(@RequestBody LeaveEvent leaveEvent) {
		leaveService.save(leaveEvent);	
		return new ResponseEntity<>(
				Response.<LeaveEvent>builder()
				.body(leaveEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("Meeting %s inserito nel calendario", leaveEvent.getID()))
				.build(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/leave", params = {"creator", "from", "to", "type"})
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByCreator(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@RequestParam ContactDto creator,
			@RequestParam CalendarEventType type) {
		List<LeaveEventDto> events = leaveService.findAllByDateCreatorType(from, to, creator, type);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder()
				.body(events)
				.code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi %s di %s dal %s al %s", creator))
				.build(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/leave", params = {"creator"})
	public ResponseEntity<Response<List<LeaveEventDto>>> findAllByCreator(@RequestParam ContactDto creator) {
		List<LeaveEventDto> events = leaveService.findAllByEventCreator(creator);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder()
				.body(events)
				.code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi di %s", creator))
				.build(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/meeting", params = {"from", "to", "creators", "type"})
	public ResponseEntity<Response<List<LeaveEventDto>>> getAllInDateRangeByCreatorList(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@RequestParam List<ContactDto> creators,
			@RequestParam CalendarEventType type) {
		
		List<LeaveEventDto> events = leaveService.findAllByDateCreatorListType(from, to, creators, type);
		return new ResponseEntity<>(
				Response.<List<LeaveEventDto>>builder()
				.body(events)
				.description(String.format("Lista dei permessi %s di %s tra il %s e il %s", type, creators, from, to))
				.code(HttpStatus.OK.value())
				.build(), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/leave")
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
