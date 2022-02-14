package com.perigea.tracker.calendar.rest;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.calendar.service.SchedulerService;



@RestController
public class SchedulerRestController {
	
	@Autowired
	private SchedulerService service;
	
//	// intervallo numero di giorni? ore? boh
//	@GetMapping(path = "schedule")
//	public ResponseEntity<ScheduledEvent> schedule(
//			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date data) {
//		
//		return new ResponseEntity<>(service.scheduleNotifica(data), HttpStatus.OK);
//	}
//	
//	@GetMapping(path = "schedule_periodico")
//	public ResponseEntity<ScheduledEvent> schedulePeriodico(@RequestParam String cron) {
//		
//		// FIXME NextFireTime = null
//		return new ResponseEntity<>(service.scheduleNotificaPeriodica(cron), HttpStatus.OK);
//	}
//	
	@GetMapping(path = "pause")
	public ResponseEntity<String> pause(@RequestParam String id) throws SchedulerException{
		
		return new ResponseEntity<>(service.pauseNotification(id), HttpStatus.OK);
	}
	
	@GetMapping(path = "delete")
	public ResponseEntity<String> delete(@RequestParam String id) throws SchedulerException{
		
		return new ResponseEntity<>(service.disactiveNotification(id) ? "Deleted" : "Not found", HttpStatus.OK);
	}
	
//	@GetMapping(path = "reschedule", params = {"id", "data"})
//	public ResponseEntity<ScheduledEvent> reschedule(@RequestParam String id,
//			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data) throws SchedulerException {
//		
//		return new ResponseEntity<>(service.reschedule(data, id), HttpStatus.OK);
//	}
	
	@GetMapping(path = "reschedule", params = {"id", "cron"})
	public ResponseEntity<ScheduledEvent> reschedule(@RequestParam String id,
			@RequestParam String cron) throws SchedulerException {
		
		return new ResponseEntity<>(service.reschedule(cron, id),	HttpStatus.OK);
	}
	
	@GetMapping(path = "tutti/")
	public ResponseEntity<List<ScheduledEvent>> tutti(){
		List<ScheduledEvent> tutti = service.getAll();
		
		return new ResponseEntity<>(tutti, HttpStatus.OK);
	}
}
