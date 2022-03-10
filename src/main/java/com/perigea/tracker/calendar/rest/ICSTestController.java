package com.perigea.tracker.calendar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.factory.ICSFactory;
import com.perigea.tracker.calendar.mapper.MeetingMapper;
import com.perigea.tracker.calendar.service.MeetingEventService;

@RestController
public class ICSTestController {
	
	@Autowired
	private MeetingEventService service;
	
	@Autowired
	private MeetingMapper mapper;
	
	@GetMapping(path = "ics/{id}")
	public String getICS(@PathVariable String id) {
		return new String(ICSFactory.createICS(mapper.mapToDto(service.findById(id))));
	}
}
