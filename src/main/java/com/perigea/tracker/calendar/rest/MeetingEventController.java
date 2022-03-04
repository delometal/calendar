package com.perigea.tracker.calendar.rest;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.mapper.MeetingMapper;
import com.perigea.tracker.calendar.service.EventEmailBuilderService;
import com.perigea.tracker.calendar.service.MeetingEventService;
import com.perigea.tracker.calendar.service.MeetingRoomService;
import com.perigea.tracker.calendar.service.SchedulerService;
import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.model.Email;
import com.perigea.tracker.commons.utils.Utils;

@RestController
@RequestMapping(path = "/meeting")
public class MeetingEventController {

	@Autowired
	private MeetingEventService meetingService;
	
	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private EventEmailBuilderService emailBuilder;
	
	// TODO notificationService come nome
	@Autowired
	private NotificationRestClient notificator;
	
	@Autowired
	private MeetingRoomService roomService;
	
	@Autowired
	private MeetingMapper mapper;


	@PostMapping(path = "/create-meeting")
	public ResponseEntity<ResponseDto<MeetingEventDto>> addMeeting(@RequestBody MeetingEventDto meetingEvent) {

		MeetingEvent event = mapper.mapToEntity(meetingEvent);
		Email email = emailBuilder.build(event, "creato");		
		Date notificationDate = Utils.shiftTime(event.getStartDate(), Utils.NOTIFICATION_SHIFT_AMOUNT);

		meetingService.save(event);
		notificator.send(email);
		schedulerService.scheduleNotifica(notificationDate, emailBuilder.buildReminder(event));
		return new ResponseEntity<>(ResponseDto.<MeetingEventDto>builder().data(meetingEvent).code(HttpStatus.OK.value())
				.description("Meeting inserito nel calendario").build(), HttpStatus.OK);
	}
	
	@PostMapping(path = "/create-periodic-meeting")
	public ResponseEntity<ResponseDto<MeetingEventDto>> addPeriodicMeeting(@RequestBody MeetingEventDto meetingEvent,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date expiration, @RequestParam String cron) {

		MeetingEvent event = mapper.mapToEntity(meetingEvent);
		Email email = emailBuilder.build(event, "creato");		

		meetingService.save(event);
		notificator.send(email);
		schedulerService.scheduleNotificaPeriodica(cron, emailBuilder.buildReminder(event), expiration);
		return new ResponseEntity<>(ResponseDto.<MeetingEventDto>builder().data(meetingEvent).code(HttpStatus.OK.value())
				.description("Meeting inserito nel calendario").build(), HttpStatus.OK);
	}
	
	@PutMapping(path = "/update-meeting")
	public ResponseEntity<ResponseDto<MeetingEventDto>> updateMeeting(@RequestBody MeetingEventDto meetingEvent) {
		MeetingEvent event = mapper.mapToEntity(meetingEvent);
		Email email = emailBuilder.build(event, "modificato");
		Date notificationDate = Utils.shiftTime(event.getStartDate(), Utils.NOTIFICATION_SHIFT_AMOUNT);

		meetingService.update(event);
		notificator.send(email);
		schedulerService.reschedule(notificationDate, email.getEventId(), emailBuilder.buildReminder(event));
		return new ResponseEntity<>(ResponseDto.<MeetingEventDto>builder().data(meetingEvent).code(HttpStatus.OK.value())
				.description("Meeting aggiornato nel calendario").build(), HttpStatus.OK);
	}

	@DeleteMapping(path = "/delete-meeting")
	public ResponseEntity<ResponseDto<String>> deleteMeeting(@RequestBody MeetingEventDto toBeDeleted, @RequestBody List<String> filePath) {
		MeetingEvent event = mapper.mapToEntity(toBeDeleted);
		meetingService.delete(event);
		Email email = emailBuilder.build(event, "eliminato");
		notificator.send(email);
		boolean deleted = schedulerService.disactiveNotification(toBeDeleted.getId());
		return new ResponseEntity<>(
				ResponseDto.<String>builder().data(deleted ? "OK" : "Error")
						.code(deleted ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
						.build(),
				HttpStatus.OK);
	}

	
	@GetMapping(path = "/get-by-creator/{mailCreator}")
	public ResponseEntity<ResponseDto<List<MeetingEventDto>>> getAllByCreator(@PathVariable String mailCreator) {
		// finestra di tempo simmetrica di due mesi
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -1);
		Date from = cal.getTime();
		cal.add(Calendar.MONTH, 2);
		Date to = cal.getTime();
		return getAllInDateRangeByCreator(from, to, mailCreator);
	}

	@GetMapping(path = "/get-meetings-in-date-range/{from}/{to}")
	public ResponseEntity<ResponseDto<List<MeetingEventDto>>> getAllInDateRange(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to) {

		List<MeetingEvent> events = meetingService.getEventsBetween(from, to);
		List<MeetingEventDto> meetings = mapper.mapToDtoList(events);
		return new ResponseEntity<ResponseDto<List<MeetingEventDto>>>(ResponseDto.<List<MeetingEventDto>>builder()
				.data(meetings).description(String.format("Tutti i meeting tra il %s e il %s", from, to))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

	@GetMapping(path = "/all-by-range-and-creator/{from}/{to}/{mailCreator}")
	public ResponseEntity<ResponseDto<List<MeetingEventDto>>> getAllInDateRangeByCreator(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@PathVariable String mailCreator) {

		List<MeetingEvent> events = meetingService.getEventsBetweenByCreator(from, to, mailCreator);
		List<MeetingEventDto> meetings = mapper.mapToDtoList(events);
		return new ResponseEntity<ResponseDto<List<MeetingEventDto>>>(ResponseDto.<List<MeetingEventDto>>builder()
				.data(meetings)
				.description(String.format("Tutti i meeting di %s tra il %s e il %s", mailCreator, from, to))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

	@GetMapping(path = "/room-availability-in-range/{from}/{to}")
	public ResponseEntity<ResponseDto<Boolean>> checkAvailability(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to) {
		return new ResponseEntity<>(ResponseDto.<Boolean>builder().data(roomService.isFree(from, to))
				.description(String.format("Disponibiltà sala riunioni da %s a %s", from.toString(), to.toString()))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

	@GetMapping(path = "/room-availability/{instant}")
	public ResponseEntity<ResponseDto<Boolean>> checkAvailability(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date instant) {

		return new ResponseEntity<>(ResponseDto.<Boolean>builder().data(roomService.isFree(instant))
				.description(String.format("Disponibiltà sala riunioni in data %s", instant.toString()))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

}
