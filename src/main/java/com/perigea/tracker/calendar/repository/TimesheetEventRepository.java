package com.perigea.tracker.calendar.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.perigea.tracker.calendar.entity.TimesheetEvent;

public interface TimesheetEventRepository extends MongoRepository<TimesheetEvent, String> {

	@Query(value = "{ 'eventCreator.mailAziendale': ?0}")
	public List<TimesheetEvent> findAllByEventCreator(String mailAziendale);
}
