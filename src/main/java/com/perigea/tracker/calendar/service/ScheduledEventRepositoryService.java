package com.perigea.tracker.calendar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.calendar.repository.ScheduledEventRepository;
import com.perigea.tracker.commons.enums.EventStatus;

@Service
public class ScheduledEventRepositoryService {

	@Autowired
	private ScheduledEventRepository repo;
	

	public List<ScheduledEvent> getAll() {
		return repo.findAll();
	}
	
	public ScheduledEvent getById(String id) {
		return repo.findById(id).orElseThrow();
	}

	public void deleteJobById(String id) {
		repo.deleteById(id);
	}

	public void deleteAll(List<ScheduledEvent> list) {
		repo.deleteAll(list);
	}

	public void save(ScheduledEvent info) {
		repo.save(info);
	}

	public List<ScheduledEvent> getAllByStatus(EventStatus status) {
		return repo.findAllByStatus(status);
	}
	
	public ScheduledEvent getByStatusAndId(EventStatus status, String id) {
		return repo.findByStatusAndId(status, id);
	}
	



}
