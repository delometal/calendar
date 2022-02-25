package com.perigea.tracker.calendar.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.repository.MeetingEventRepository;

@Service
public class MeetingRoomService {
	
	// TODO stato sala riunioni da-a, non solo bool
	
	@Autowired
	private MeetingEventRepository repository;
	
	public boolean isFree(Date from, Date to) {
		return repository.blockingMeetingsInRange(from, to).isEmpty();
	}
	
	public boolean isFree(Date instant) {
		return repository.blockingMeetingsAtInstant(instant).isEmpty();
	}
}
