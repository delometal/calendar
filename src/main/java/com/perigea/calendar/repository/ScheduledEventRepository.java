package com.perigea.calendar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perigea.calendar.entity.ScheduledEvent;

public interface ScheduledEventRepository extends MongoRepository<ScheduledEvent, String>{
	
}