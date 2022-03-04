package com.perigea.tracker.calendar.rest;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.calendar.mapper.ScheduleMapper;
import com.perigea.tracker.calendar.service.SchedulerService;
import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.dto.ScheduledEventDto;
import com.perigea.tracker.commons.model.Email;

@RestController
@RequestMapping("/scheduler")
public class SchedulerRestController {

	@Autowired
	private SchedulerService service;
	
	@Autowired
	private ScheduleMapper scheduleMapper;

	@PostMapping(value = "/schedule-notifica")
	public ResponseEntity<ResponseDto<ScheduledEventDto>> schedule(@RequestBody Email email) {
		ScheduledEvent event = service.scheduleNotifica(email.getReminderDate(), email);
		ScheduledEventDto dto = scheduleMapper.mapToDto(event);
		ResponseDto<ScheduledEventDto> genericDto = ResponseDto.<ScheduledEventDto>builder().data(dto).build();
		return ResponseEntity.ok(genericDto);
	}

//	
//	@GetMapping(path = "schedule_periodico")
//	public ResponseEntity<ScheduledEvent> schedulePeriodico(@RequestParam String cron) {
//		
//		
//		return new ResponseEntity<>(service.scheduleNotificaPeriodica(cron), HttpStatus.OK);
//	}
//	
	@GetMapping(path = "pause/{id}")
	public ResponseEntity<String> pause(@PathVariable String id) throws SchedulerException {

		return new ResponseEntity<>(service.pauseNotification(id), HttpStatus.OK);
	}

	@GetMapping(path = "delete/{id}")
	public ResponseEntity<String> delete(@PathVariable String id) throws SchedulerException {

		return new ResponseEntity<>(service.disactiveNotification(id) ? "Deleted" : "Not found", HttpStatus.OK);
	}

//	@GetMapping(path = "reschedule", params = {"id", "data"})
//	public ResponseEntity<ScheduledEvent> reschedule(@RequestParam String id,
//			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data) throws SchedulerException {
//		
//		return new ResponseEntity<>(service.reschedule(data, id), HttpStatus.OK);
//	}

	@GetMapping(path = "reschedule/{id}/{cron}")
	public ResponseEntity<ScheduledEvent> reschedule(@PathVariable String id, @PathVariable String cron)
			throws SchedulerException {

		return new ResponseEntity<>(service.reschedule(cron, id), HttpStatus.OK);
	}

	@GetMapping(path = "tutti/")
	public ResponseEntity<List<ScheduledEvent>> tutti() {
		List<ScheduledEvent> tutti = service.getAll();

		return new ResponseEntity<>(tutti, HttpStatus.OK);
	}
}
