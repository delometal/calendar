package com.perigea.tracker.calendar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.commons.dto.EventContactDto;
import com.perigea.tracker.commons.enums.CalendarEventType;

public interface LeaveEventRepository extends MongoRepository<LeaveEvent, String> {

	public List<LeaveEvent> findAllByType(CalendarEventType type);
	
	//TODO alcuni dei seguenti metodi si possono estrarre e inserire in un'interfaccia comune a questo e l'altro repo
	//lo stesso vale per i service
	public List<LeaveEvent> findAllByStartDateBetween(Date from, Date to);
	
	public List<LeaveEvent> findAllByEventCreator(EventContactDto creator);
	
	//FIXME i seguenti tre non sono utilizzati pare
	//TODO vedere come fare: le query in teoria vanno bene così, ma l'oggetto deve essere identico a quello inserito.
	//l'alternativa è passare qui stringa e poi nella query cercare con dot notation (e nel caso della lista
	//spremere fuori gli id)
	@Query(value = "{'eventCreator': ?2, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<LeaveEvent> findAllByStartDateBetweenByCreator(Date from, Date to, EventContactDto creator);
	
	@Query(value = "{'eventCreator': {$in: ?2}, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<LeaveEvent> findAllByStartDateBetweenByCreatorList(Date from, Date to, List<EventContactDto> creatorList);
	
	@Query(value = "{'eventCreator': {$in: ?0}}")
	public List<LeaveEvent> findAllByCreatorList(List<EventContactDto> creatorList);
	
	@Query(value = "{'eventCreator': ?2, 'type': ?3, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<LeaveEvent> findAllByStartDateBetweenByCreatorByType(Date from, Date to, EventContactDto creator, CalendarEventType type);
	
	@Query(value = "{'eventCreator': {$in: ?2}, 'type': ?3, 'startDate': {$gt: ?0, $lt: ?1}}")
	public List<LeaveEvent> findAllByStartDateBetweenByCreatorListByType(Date from, Date to, List<EventContactDto> creators,
			CalendarEventType type);
}
