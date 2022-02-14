package com.perigea.tracker.calendar.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.commons.enums.EventStatus;

public interface ScheduledEventRepository extends MongoRepository<ScheduledEvent, String>{
	
	@Query(value = "{ 'status': ?0}")
	public List<ScheduledEvent> findAllByStatus(EventStatus status);
	
	@Query(value = "{ 'status': ?0, 'id': ?1}")
	public ScheduledEvent findByStatusAndId(EventStatus status, String id);
}