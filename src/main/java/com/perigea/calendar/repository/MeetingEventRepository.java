package com.perigea.calendar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.perigea.calendar.dto.ContactDto;
import com.perigea.calendar.entity.MeetingEvent;

@Repository
public interface MeetingEventRepository extends MongoRepository<MeetingEvent, String> {

	public List<MeetingEvent> findAllByStartDateBetween(Date from, Date to);
	
	public List<MeetingEvent> findAllByEventCreator(String eventCreator);
	
	//TODO vedere come fare: le query in teoria vanno bene così, ma l'oggetto deve essere identico a quello inserito.
	//l'alternativa è passare qui stringa e poi nella query cercare con dot notation (e nel caso della lista
	//spremere fuori gli id)
	@Query(value = "{'eventCreator': ?2, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<MeetingEvent> findAllByStartDateBetweenByCreator(Date from, Date to, ContactDto creator);
	
	@Query(value = "{'eventCreator': {$in: ?2}, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<MeetingEvent> findAllByStartDateBetweenByCreatorList(Date from, Date to, List<ContactDto> creators);
	
	@Query(value = "{'meetingRoom':true,  $or:[{'startDate':{$gt:?0, $lt:?1}}, {'endDate':{$gt:?0, $lt:?1}}]}")
	public List<MeetingEvent> blockingMeetingsInRange(Date from, Date to);
	
	@Query(value = "{'meetingRoom': true, 'startDate': {$lt: ?0}, 'endDate': {$gt: ?0}}")
	public List<MeetingEvent> blockingMeetingsAtInstant(Date instant);
	
}
