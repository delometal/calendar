package com.perigea.tracker.calendar.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.perigea.tracker.calendar.entity.MeetingEvent;

@Repository
public interface MeetingEventRepository extends MongoRepository<MeetingEvent, String> {

	public List<MeetingEvent> findAllByStartDateBetween(LocalDateTime from, LocalDateTime to);

	@Query(value = "{'eventCreator.mailAziendale': ?0}")
	public List<MeetingEvent> findAllByEventCreator(String eventCreator);

	@Query(value = "{'eventCreator.mailAziendale': ?2, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<MeetingEvent> findAllByStartDateBetweenByCreator(LocalDateTime from, LocalDateTime to, String mailAziendale);

	@Query(value = "{'meetingRoom':true,  $or:[{'startDate':{$gt:?0, $lt:?1}}, {'endDate':{$gt:?0, $lt:?1}}]}")
	public List<MeetingEvent> blockingMeetingsInRange(LocalDateTime from, LocalDateTime to);

	@Query(value = "{'meetingRoom': true, 'startDate': {$lt: ?0}, 'endDate': {$gt: ?0}}")
	public List<MeetingEvent> blockingMeetingsAtInstant(LocalDateTime instant);

}
