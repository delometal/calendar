package com.perigea.tracker.calendar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perigea.tracker.calendar.entity.ScheduledEvent;

public interface ScheduledEventRepository extends MongoRepository<ScheduledEvent, String>{
	
}