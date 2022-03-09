package com.perigea.tracker.calendar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.perigea.tracker.calendar.entity.HolidayRequestEvent;
import com.perigea.tracker.commons.enums.CalendarEventType;

public interface HolidayEventRepository extends MongoRepository<HolidayRequestEvent, String> {

	public List<HolidayRequestEvent> findAllByType(CalendarEventType type);

	public List<HolidayRequestEvent> findAllByStartDateBetween(Date from, Date to);

	@Query(value = "{ 'eventCreator.mailAziendale': ?0}")
	public List<HolidayRequestEvent> findAllByEventCreator(String mailAziendale);

	@Query(value = "{'eventCreator.mailAziendale': ?2, 'type': ?3, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<HolidayRequestEvent> findAllByStartDateBetweenByCreatorByType(Date from, Date to, String mailAziendale, CalendarEventType type);

	@Query(value = "{'responsabile.mailAziendale' : ?0}")
	public List<HolidayRequestEvent> findByResponsabile(String mailAziendale);

	@Query(value = "{'responsabile.mailAziendale': ?2, 'type': ?3, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<HolidayRequestEvent> findAllByStartDateBetweenByResponsabileByType(Date from, Date to, String mailAziendale, CalendarEventType type);
}
