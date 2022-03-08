package com.perigea.tracker.calendar.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.perigea.tracker.calendar.entity.HolidayEvent;
import com.perigea.tracker.calendar.mapper.HolidayMapper;
import com.perigea.tracker.calendar.service.EventEmailBuilderService;
import com.perigea.tracker.calendar.service.HolidayEventService;
import com.perigea.tracker.commons.dto.HolidayEventDto;
import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.model.Email;
import com.perigea.tracker.commons.utils.Utils;

@RestController
@RequestMapping(path = "/holiday")
public class HolidayEventController {

	@Autowired
	private HolidayEventService holidayEventService;

	@Autowired
	private HolidayMapper holidayMapper;

	@Autowired
	private EventEmailBuilderService emailBuilder;
	
	@Autowired
	private NotificationRestClient notificator;
	
	@PostMapping(path = "/add")
	public ResponseEntity<ResponseDto<HolidayEventDto>> addHolidayEvent(@RequestBody HolidayEventDto HolidayEvent) {
		HolidayEvent event = holidayMapper.mapToEntity(HolidayEvent);
		Email email = emailBuilder.build(event, "creato");
		notificator.send(email);
		holidayEventService.save(event);
		return new ResponseEntity<>(
				ResponseDto.<HolidayEventDto>builder().data(HolidayEvent).code(HttpStatus.OK.value())
						.description(String.format("%s %s salvato", HolidayEvent.getType(),HolidayEvent.getId())).build(),
				HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/delete")
	public ResponseEntity<ResponseDto<HolidayEventDto>> deleteHolidayEvent(@RequestBody HolidayEventDto HolidayEvent) {
		HolidayEvent toBeDeleted = holidayMapper.mapToEntity(HolidayEvent);
		holidayEventService.delete(toBeDeleted);
		Email email = emailBuilder.build(toBeDeleted, "eliminato");
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<HolidayEventDto>builder().data(HolidayEvent).code(HttpStatus.OK.value())
				.description(String.format("%s eliminato", HolidayEvent.getType())).build(), HttpStatus.OK);
	}

	@PutMapping(path = "/update")
	public ResponseEntity<ResponseDto<HolidayEventDto>> updateHolidayEvent(@RequestBody HolidayEventDto HolidayEvent) {
		HolidayEvent toBeUpdated = holidayMapper.mapToEntity(HolidayEvent);
		holidayEventService.update(toBeUpdated);
		Email email = emailBuilder.build(toBeUpdated, "modificato");
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<HolidayEventDto>builder().data(HolidayEvent).code(HttpStatus.OK.value())
				.description(String.format("%s %s aggiornato", HolidayEvent.getType(),HolidayEvent.getId())).build(), 
				HttpStatus.OK);
	}
	
	@PostMapping(path = "/approve")
	public ResponseEntity<ResponseDto<HolidayEventDto>> approveEvent(@RequestBody HolidayEventDto holidayEvent) {
		HolidayEvent toBeApproved = holidayMapper.mapToEntity(holidayEvent);
		holidayEventService.update(toBeApproved);
		Email email = emailBuilder.buildApproval(toBeApproved);
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<HolidayEventDto>builder().data(holidayEvent).code(HttpStatus.OK.value())
				.description(String.format("Stato di approvazione: %s", holidayEvent.getApproved())).build(), 
				HttpStatus.OK);
	}
	
	@GetMapping(path = "/get-by-date-creator-type/{mailAziendaleCeator}/{from}/{to}/{type}")
	public ResponseEntity<ResponseDto<List<HolidayEventDto>>> findAllByCreatorBetweenDates(
			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date from,
			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date to,
			@PathVariable String mailAziendaleCeator, @PathVariable CalendarEventType type) {
		List<HolidayEvent> events = holidayEventService.findAllByDateCreatorType(from, to, mailAziendaleCeator, type);
		List<HolidayEventDto> leaves = holidayMapper.mapToDtoList(events);
		return new ResponseEntity<>(ResponseDto.<List<HolidayEventDto>>builder().data(leaves).code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi %s di %s dal %s al %s",type, mailAziendaleCeator, from, to)).build(),
				HttpStatus.OK);
	}

	@GetMapping(path = "/get-by-date-responsabile-type/{mailAziendaleResponsabile}/{from}/{to}/{type}")
	public ResponseEntity<ResponseDto<List<HolidayEventDto>>> findAllByResponsabileBewtweenDates(
			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date from,
			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date to,
			@PathVariable String mailAziendaleResponsabile, @PathVariable CalendarEventType type) {
		List<HolidayEvent> events = holidayEventService.findAllByDateResponsabileType(from, to, mailAziendaleResponsabile, type);
		List<HolidayEventDto> leaves = holidayMapper.mapToDtoList(events);
		return new ResponseEntity<>(ResponseDto.<List<HolidayEventDto>>builder().data(leaves).code(HttpStatus.OK.value())
				.description(String.format("Lista dei permessi %s di %s dal %s al %s",type, mailAziendaleResponsabile, from, to))
				.build(), HttpStatus.OK);
	}

	@GetMapping(path = "/get-by-event-creator/{mailAziendaleCreator}")
	public ResponseEntity<ResponseDto<List<HolidayEventDto>>> findAllByCreator(@PathVariable String mailAziendaleCreator) {
		List<HolidayEvent> events = holidayEventService.findAllByEventCreator(mailAziendaleCreator);
		List<HolidayEventDto> holidays = holidayMapper.mapToDtoList(events);
		return new ResponseEntity<>(
				ResponseDto.<List<HolidayEventDto>>builder().data(holidays).code(HttpStatus.OK.value())
						.description(String.format("Lista dei permessi di %s", mailAziendaleCreator)).build(),
				HttpStatus.OK);
	}

	@GetMapping(path = "/get-by-responsabile/{mailAziendaleResponsabile}")
	public ResponseEntity<ResponseDto<List<HolidayEventDto>>> findAllByResponsabile(
			@PathVariable String mailAziendaleResponsabile) {
		List<HolidayEvent> events = holidayEventService.findAllByResponsabile(mailAziendaleResponsabile);
		List<HolidayEventDto> leaves = holidayMapper.mapToDtoList(events);
		return new ResponseEntity<>(
				ResponseDto.<List<HolidayEventDto>>builder().data(leaves).code(HttpStatus.OK.value())
						.description(String.format("Lista dei permessi di %s", mailAziendaleResponsabile)).build(),
				HttpStatus.OK);
	}

}
