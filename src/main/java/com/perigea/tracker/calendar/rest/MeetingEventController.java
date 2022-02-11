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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.mapper.MeetingMapper;
import com.perigea.tracker.calendar.service.EmailBuilderService;
import com.perigea.tracker.calendar.service.MeetingEventService;
import com.perigea.tracker.calendar.service.MeetingRoomService;
import com.perigea.tracker.calendar.service.SchedulerService;
import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.model.Email;

@RestController
public class MeetingEventController {

	@Autowired
	private MeetingEventService meetingService;
	
	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private EmailBuilderService emailBuilder;
	
	@Autowired
	private NotificationRestClient notificator;
	
	@Autowired
	private MeetingRoomService roomService;
	
	@Autowired
	private MeetingMapper mapper;

	@PostMapping(path = "/meeting/create-meeting")
	public ResponseEntity<Response<MeetingEventDto>> addMeeting(@RequestBody MeetingEventDto meetingEvent) {

		MeetingEvent event = mapper.mapToEntity(meetingEvent);
		meetingService.save(event);
		Email email = emailBuilder.buildFromMeetingEvent(event, "creato");
		//notificator.mandaNotifica(email);
		Calendar cal = Calendar.getInstance();
		cal.setTime(meetingEvent.getStartDate());
		cal.add(Calendar.MINUTE, -15);
		Date notificationDate = cal.getTime();
		schedulerService.scheduleNotifica(notificationDate, email);
		return new ResponseEntity<>(Response.<MeetingEventDto>builder().body(meetingEvent).code(HttpStatus.OK.value())
				.description("Meeting inserito nel calendario").build(), HttpStatus.OK);
	}
	
	@PutMapping(path = "/meeting/update-meeting")
	public ResponseEntity<Response<MeetingEventDto>> updateMeeting(@RequestBody MeetingEventDto meetingEvent) {
		MeetingEvent event = mapper.mapToEntity(meetingEvent);
		meetingService.update(event);
		Email email = emailBuilder.buildFromMeetingEvent(event, "modificato");
		notificator.mandaNotifica(email);
		Calendar cal = Calendar.getInstance();
		cal.setTime(meetingEvent.getStartDate());
		cal.add(Calendar.MINUTE, -15);
		Date notificationDate = cal.getTime();
		schedulerService.reschedule(notificationDate, email.getEventID(), email);
		return new ResponseEntity<>(Response.<MeetingEventDto>builder().body(meetingEvent).code(HttpStatus.OK.value())
				.description("Meeting aggiornato nel calendario").build(), HttpStatus.OK);
	}

	@DeleteMapping(path = "/meeting/delete-meeting")
	public ResponseEntity<Response<String>> deleteMeeting(@RequestBody MeetingEventDto toBeDeleted) {
		MeetingEvent event = mapper.mapToEntity(toBeDeleted);
		meetingService.delete(event);
		Email email = emailBuilder.buildFromMeetingEvent(event, "eliminato");
		notificator.mandaNotifica(email);
		boolean deleted = schedulerService.deleteNotifica(toBeDeleted.getID());
		return new ResponseEntity<>(
				Response.<String>builder().body(deleted ? "OK" : "Error")
						.code(deleted ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
						.build(),
				HttpStatus.OK);
	}

	
	@GetMapping(path = "/meeting/get-by-creator", params = { "creator" })
	public ResponseEntity<Response<List<MeetingEventDto>>> getAllByCreator(@RequestParam String mailAziendaleCreator) {
		// finestra di tempo simmetrica di due mesi
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -1);
		Date from = cal.getTime();
		cal.add(Calendar.MONTH, 2);
		Date to = cal.getTime();

		return getAllInDateRangeByCreator(from, to, mailAziendaleCreator);
	}

	@GetMapping(path = "/meeting/get-meetings-in-date-range", params = { "from", "to" })
	public ResponseEntity<Response<List<MeetingEventDto>>> getAllInDateRange(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to) {

		List<MeetingEvent> events = meetingService.getEventsBetween(from, to);
		List<MeetingEventDto> meetings = mapper.mapToDtoList(events);
		return new ResponseEntity<Response<List<MeetingEventDto>>>(Response.<List<MeetingEventDto>>builder()
				.body(meetings).description(String.format("Tutti i meeting tra il %s e il %s", from, to))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

	@GetMapping(path = "/meeting", params = { "from", "to", "creator" })
	public ResponseEntity<Response<List<MeetingEventDto>>> getAllInDateRangeByCreator(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to,
			@RequestParam String mailAziendaleCreator) {

		List<MeetingEvent> events = meetingService.getEventsBetweenByCreator(from, to, mailAziendaleCreator);
		List<MeetingEventDto> meetings = mapper.mapToDtoList(events);
		return new ResponseEntity<Response<List<MeetingEventDto>>>(Response.<List<MeetingEventDto>>builder()
				.body(meetings)
				.description(String.format("Tutti i meeting di %s tra il %s e il %s", mailAziendaleCreator, from, to))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

	@GetMapping(path = "/room-availability-in-range", params = { "from", "to" })
	public ResponseEntity<Response<Boolean>> checkAvailability(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date to) {
		return new ResponseEntity<>(Response.<Boolean>builder().body(roomService.isFree(from, to))
				.description(String.format("Disponibiltà sala riunioni da %s a %s", from.toString(), to.toString()))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

	@GetMapping(path = "/room-availability", params = { "instant" })
	public ResponseEntity<Response<Boolean>> checkAvailability(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date instant) {

		return new ResponseEntity<>(Response.<Boolean>builder().body(roomService.isFree(instant))
				// TODO dicitura un po' fuorviante
				.description(String.format("Disponibiltà sala riunioni in data %s", instant.toString()))
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

}
