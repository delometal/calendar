package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.dto.ContactDto;
import com.perigea.tracker.calendar.dto.MeetingEventDto;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.enums.CalendarEventType;
import com.perigea.tracker.calendar.enums.ParticipationStatus;
import com.perigea.tracker.calendar.exception.EntityNotFoundException;
import com.perigea.tracker.calendar.exception.MeetingEventException;
import com.perigea.tracker.calendar.mapper.MeetingMapper;
import com.perigea.tracker.calendar.repository.MeetingEventRepository;

@Service
public class MeetingEventService {

	@Autowired
	private MeetingEventRepository repository;
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private MeetingMapper mapper;
	
	public MeetingEventDto save(MeetingEvent event) {
		MeetingEventDto eventAdded = mapper.mapToDto(repository.save(event));
		logger.info(String.format("Evento %s aggiunto in persistenza", event.getID()));
		return eventAdded;
	}
	
	public void delete(MeetingEvent event) {
		repository.delete(event);
		logger.info(String.format("Evento %s cancellato", event.getID()));
	}
	
	public List<MeetingEventDto> getEventsBetween(Date from, Date to) {
		try {	
			List<MeetingEvent> events =  repository.findAllByStartDateBetween(from, to);
			return mapper.mapToDtoList(events);
		} catch (Exception ex) {
			throw new MeetingEventException(ex.getMessage());
		}
	}
	
	public List<MeetingEventDto> findAll(CalendarEventType type) {
		try {	
			return mapper.mapToDtoList(repository.findAll());
		}catch (Exception ex) {
			throw new MeetingEventException(ex.getMessage());
		}
	}
	
	public List<MeetingEventDto> findAllByCreator(String eventCreator){
		try {	
			return mapper.mapToDtoList(repository.findAllByEventCreator(eventCreator));
		}catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new MeetingEventException(ex.getMessage());
		}
	}
	
	public List<MeetingEventDto> getEventsBetweenByCreator(Date from, Date to, ContactDto creator) {
		try {
			return mapper.mapToDtoList(repository.findAllByStartDateBetweenByCreator(from, to, creator));
		}catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new MeetingEventException(ex.getMessage());
		}
	}
	

	public List<MeetingEventDto> getEventsBetweenByCreatorList(Date from, Date to, List<ContactDto> creators) {
		try {
			return mapper.mapToDtoList(repository.findAllByStartDateBetweenByCreatorList(from, to, creators));
		}catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new MeetingEventException(ex.getMessage());
		}
	}
	
	public MeetingEventDto findById(String meetingId) {
			try {
			Optional<MeetingEvent> optionalEvent = repository.findById(meetingId);
			return optionalEvent.isPresent() ? mapper.mapToDto(optionalEvent.get()) : null;
		}catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new MeetingEventException(ex.getMessage());
		}
	}

	public boolean acceptInvite(String meetingId, String participantId) {
		return changeInviteStatus(meetingId, participantId, ParticipationStatus.Confermata);
	}

	public boolean declineInvite(String meetingId, String participantId) {
		return changeInviteStatus(meetingId, participantId, ParticipationStatus.Non_confermata);
	}

	private boolean changeInviteStatus(String meetingId, String participantId, ParticipationStatus status) {
		MeetingEventDto event = findById(meetingId);
		if (event == null) {
			throw new EntityNotFoundException("Meeting non trovato");
		}
		List<ContactDto> participants = event.getParticipants();
		
		Optional<ContactDto> optionalParticipant = participants.stream()
				.filter(p -> p.getCodicePersona().equals(participantId)).findFirst();

		if (optionalParticipant.isEmpty()) {
			throw new EntityNotFoundException("Partecipante non trovato");
		}
	
		ContactDto participant = optionalParticipant.get();
		participant.setParticipationStatus(status);
		participants.removeIf(p -> p.getCodicePersona().equals(participantId));
		participants.add(participant);
		event.setParticipants(participants);
		repository.save(mapper.mapToEntity(event));
		logger.info(String.format("Evento %s aggiornato", event.getID()));
		return true;
	}
}
