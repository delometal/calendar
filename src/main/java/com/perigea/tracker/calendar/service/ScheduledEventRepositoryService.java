package com.perigea.tracker.calendar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.calendar.repository.ScheduledEventRepository;


@Service
public class ScheduledEventRepositoryService {
	
	@Autowired
	private ScheduledEventRepository repo;
	
	public List<ScheduledEvent> getAll() {
		return repo.findAll();
	}
	
	public void deleteJobById(String id) {
		repo.deleteById(id);
	}
	
	public void deleteAll() {
		repo.deleteAll();
	}
	
	public void save(ScheduledEvent info) {
		repo.save(info);
	}
}
