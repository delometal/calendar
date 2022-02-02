package com.perigea.tracker.calendar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.factory.ICSFactory;
import com.perigea.tracker.calendar.service.MeetingEventService;

@RestController
public class ICSTestController {
	
	@Autowired
	private MeetingEventService service;
	
	@GetMapping(path = "ics")
	public String getICS(@RequestParam String id) {
		return new String(ICSFactory.createICS(service.findById(id)));
	}
}
