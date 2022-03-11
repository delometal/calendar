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

import com.perigea.tracker.calendar.entity.HolidayRequestEvent;
import com.perigea.tracker.calendar.mapper.HolidayEventMapper;
import com.perigea.tracker.calendar.mapper.HolidayMapper;
import com.perigea.tracker.calendar.model.HolidayEvent;
import com.perigea.tracker.calendar.service.EventEmailBuilderService;
import com.perigea.tracker.calendar.service.HolidayEventService;
import com.perigea.tracker.commons.dto.HolidayEventRequestDto;
import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.model.Email;

@RestController
@RequestMapping(path = "/holiday")
public class HolidayEventController {

	@Autowired
	private HolidayEventService holidayEventService;

	@Autowired
	private HolidayEventMapper holidayEventMapper;

	@Autowired
	private EventEmailBuilderService emailBuilder;

	@Autowired
	private NotificationRestClient notificator;

	@Autowired
	private HolidayMapper holidayMapper;

	@PostMapping(path = "/add")
	public ResponseEntity<ResponseDto<HolidayEventRequestDto>> addHolidayEvent(
			@RequestBody HolidayEventRequestDto holidayEvent) {
		HolidayRequestEvent event = holidayEventMapper.mapToEntity(holidayEvent);
		Email email = emailBuilder.build(holidayEvent, "creato");
		notificator.send(email);
		holidayEventService.save(event);
		return new ResponseEntity<>(ResponseDto.<HolidayEventRequestDto>builder().data(holidayEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("%s %s salvato", holidayEvent.getType(), holidayEvent.getId())).build(),
				HttpStatus.OK);
	}

	@DeleteMapping(path = "/delete")
	public ResponseEntity<ResponseDto<HolidayEventRequestDto>> deleteHolidayEvent(
			@RequestBody HolidayEventRequestDto holidayEvent) {
		HolidayRequestEvent toBeDeleted = holidayEventMapper.mapToEntity(holidayEvent);
		holidayEventService.delete(toBeDeleted);
		Email email = emailBuilder.build(holidayEvent, "eliminato");
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<HolidayEventRequestDto>builder().data(holidayEvent)
				.code(HttpStatus.OK.value()).description(String.format("%s eliminato", holidayEvent.getType())).build(),
				HttpStatus.OK);
	}

	@PutMapping(path = "/update")
	public ResponseEntity<ResponseDto<HolidayEventRequestDto>> updateHolidayEvent(
			@RequestBody HolidayEventRequestDto holidayEvent) {
		HolidayRequestEvent toBeUpdated = holidayEventMapper.mapToEntity(holidayEvent);
		holidayEventService.update(toBeUpdated);
		Email email = emailBuilder.build(holidayEvent, "modificato");
		notificator.send(email);
		return new ResponseEntity<>(ResponseDto.<HolidayEventRequestDto>builder().data(holidayEvent)
				.code(HttpStatus.OK.value())
				.description(String.format("%s %s aggiornato", holidayEvent.getType(), holidayEvent.getId())).build(),
				HttpStatus.OK);
	}

	@PostMapping(path = "/approve-all")
	public ResponseEntity<ResponseDto<HolidayEventRequestDto>> approveEvent(
			@RequestBody HolidayEventRequestDto holidayEvent) {
		HolidayRequestEvent toBeApproved = holidayEventMapper.mapToEntity(holidayEvent);
		holidayEventService.updateApprovalStatus(toBeApproved.getId(), toBeApproved.getApproved());
		Email email = emailBuilder.buildApproval(holidayEvent);
		notificator.send(email);
		return new ResponseEntity<>(
				ResponseDto.<HolidayEventRequestDto>builder().data(holidayEvent).code(HttpStatus.OK.value())
						.description(String.format("Stato di approvazione: %s", holidayEvent.getApproved())).build(),
				HttpStatus.OK);
	}

	@PostMapping(path = "/approve-single-event")
	public ResponseEntity<ResponseDto<HolidayEventRequestDto>> approveSingleEvent(
			@RequestBody HolidayEventRequestDto holidayEvent) {
		List<HolidayEvent> events = holidayMapper.mapToEntityList(holidayEvent.getHolidays());
		HolidayRequestEvent toBeApproved = holidayEventService.approveSingleEvent(events, holidayEvent.getId());
		Email email = emailBuilder.buildApproval(holidayEventMapper.mapToDto(toBeApproved));
		notificator.send(email);
		return new ResponseEntity<>(
				ResponseDto.<HolidayEventRequestDto>builder().data(holidayEventMapper.mapToDto(toBeApproved))
						.code(HttpStatus.OK.value())
						.description(String.format("Stato di approvazione: %s", holidayEvent.getApproved())).build(),
				HttpStatus.OK);
	}

	@PostMapping(path = "/cancel-holidays")
	public ResponseEntity<ResponseDto<HolidayEventRequestDto>> deleteSingleEvent(
			@RequestBody HolidayEventRequestDto holidayEvent) {
		Email email = emailBuilder.build(holidayEvent, "annullato");
		notificator.send(email);
		return new ResponseEntity<>(
				ResponseDto.<HolidayEventRequestDto>builder().data(holidayEvent).code(HttpStatus.OK.value())
						.description(String.format("Stato di approvazione: %s", holidayEvent.getApproved())).build(),
				HttpStatus.OK);
	}

	@PostMapping(path = "/approve-cancel-holidays")
	public ResponseEntity<ResponseDto<HolidayEventRequestDto>> approveCancel(
			@RequestBody HolidayEventRequestDto holidayEvent) {
		List<HolidayEvent> events = holidayMapper.mapToEntityList(holidayEvent.getHolidays());
		HolidayRequestEvent toBeApproved = holidayEventService.deleteSingleHolidayEvents(events, holidayEvent.getId());
		Email email = emailBuilder.buildApproval(holidayEventMapper.mapToDto(toBeApproved));
		notificator.send(email);
		return new ResponseEntity<>(
				ResponseDto.<HolidayEventRequestDto>builder().data(holidayEventMapper.mapToDto(toBeApproved))
						.code(HttpStatus.OK.value())
						.description(String.format("Stato di approvazione: %s", holidayEvent.getApproved())).build(),
				HttpStatus.OK);
	}

//	@GetMapping(path = "/get-by-date-creator-type/{mailAziendaleCeator}/{from}/{to}/{type}")
//	public ResponseEntity<ResponseDto<List<HolidayEventRequestDto>>> findAllByCreatorBetweenDates(
//			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date from,
//			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date to,
//			@PathVariable String mailAziendaleCeator, @PathVariable CalendarEventType type) {
//		List<HolidayRequestEvent> events = holidayEventService.findAllByDateCreatorType(from, to, mailAziendaleCeator, type);
//		List<HolidayEventRequestDto> leaves = holidayMapper.mapToDtoList(events);
//		return new ResponseEntity<>(ResponseDto.<List<HolidayEventRequestDto>>builder().data(leaves).code(HttpStatus.OK.value())
//				.description(String.format("Lista dei permessi %s di %s dal %s al %s",type, mailAziendaleCeator, from, to)).build(),
//				HttpStatus.OK);
//	}
//
//	@GetMapping(path = "/get-by-date-responsabile-type/{mailAziendaleResponsabile}/{from}/{to}/{type}")
//	public ResponseEntity<ResponseDto<List<HolidayEventRequestDto>>> findAllByResponsabileBewtweenDates(
//			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date from,
//			@PathVariable @DateTimeFormat(pattern = Utils.DATE_FORMAT) Date to,
//			@PathVariable String mailAziendaleResponsabile, @PathVariable CalendarEventType type) {
//		List<HolidayRequestEvent> events = holidayEventService.findAllByDateResponsabileType(from, to, mailAziendaleResponsabile, type);
//		List<HolidayEventRequestDto> leaves = holidayMapper.mapToDtoList(events);
//		return new ResponseEntity<>(ResponseDto.<List<HolidayEventRequestDto>>builder().data(leaves).code(HttpStatus.OK.value())
//				.description(String.format("Lista dei permessi %s di %s dal %s al %s",type, mailAziendaleResponsabile, from, to))
//				.build(), HttpStatus.OK);
//	}

	@GetMapping(path = "/get-by-event-creator/{mailAziendaleCreator}")
	public ResponseEntity<ResponseDto<List<HolidayEventRequestDto>>> findAllByCreator(
			@PathVariable String mailAziendaleCreator) {
		List<HolidayRequestEvent> events = holidayEventService.findAllByEventCreator(mailAziendaleCreator);
		List<HolidayEventRequestDto> holidays = holidayEventMapper.mapToDtoList(events);
		return new ResponseEntity<>(
				ResponseDto.<List<HolidayEventRequestDto>>builder().data(holidays).code(HttpStatus.OK.value())
						.description(String.format("Lista dei permessi di %s", mailAziendaleCreator)).build(),
				HttpStatus.OK);
	}

	@GetMapping(path = "/get-by-responsabile/{mailAziendaleResponsabile}")
	public ResponseEntity<ResponseDto<List<HolidayEventRequestDto>>> findAllByResponsabile(
			@PathVariable String mailAziendaleResponsabile) {
		List<HolidayRequestEvent> events = holidayEventService.findAllByResponsabile(mailAziendaleResponsabile);
		List<HolidayEventRequestDto> leaves = holidayEventMapper.mapToDtoList(events);
		return new ResponseEntity<>(
				ResponseDto.<List<HolidayEventRequestDto>>builder().data(leaves).code(HttpStatus.OK.value())
						.description(String.format("Lista dei permessi di %s", mailAziendaleResponsabile)).build(),
				HttpStatus.OK);
	}

}
