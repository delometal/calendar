package com.perigea.tracker.calendar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.perigea.tracker.calendar.entity.HolidayEvent;
import com.perigea.tracker.commons.enums.CalendarEventType;

public interface HolidayEventRepository extends MongoRepository<HolidayEvent, String> {

	public List<HolidayEvent> findAllByType(CalendarEventType type);

	// TODO alcuni dei seguenti metodi si possono estrarre e inserire in
	// un'interfaccia comune a questo e l'altro repo
	// lo stesso vale per i service
	public List<HolidayEvent> findAllByStartDateBetween(Date from, Date to);

	@Query(value = "{ 'eventCreator.mailAziendale': ?0}")
	public List<HolidayEvent> findAllByEventCreator(String mailAziendale);

	@Query(value = "{'eventCreator.mailAziendale': ?2, 'type': ?3, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<HolidayEvent> findAllByStartDateBetweenByCreatorByType(Date from, Date to, String mailAziendale, CalendarEventType type);

	@Query(value = "{'responsabile.mailAziendale' : ?0}")
	public List<HolidayEvent> findByResponsabile(String mailAziendale);

	@Query(value = "{'responsabile.mailAziendale': ?2, 'type': ?3, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<HolidayEvent> findAllByStartDateBetweenByResponsabileByType(Date from, Date to, String mailAziendale, CalendarEventType type);
}
