package com.perigea.calendar.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.calendar.repository.MeetingEventRepository;

@Service
public class MeetingRoomService {
	
	@Autowired
	private MeetingEventRepository repository;
	
	public boolean isFree(Date from, Date to) {
		return repository.blockingMeetingsInRange(from, to).isEmpty();
	}
	
	public boolean isFree(Date instant) {
		return repository.blockingMeetingsAtInstant(instant).isEmpty();
	}
}
