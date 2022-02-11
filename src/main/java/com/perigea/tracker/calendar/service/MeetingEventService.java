package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.repository.MeetingEventRepository;
import com.perigea.tracker.commons.dto.EventContactDto;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.enums.ParticipationStatus;
import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.MeetingEventException;

@Service
public class MeetingEventService {

	@Autowired
	private MeetingEventRepository repository;

	@Autowired
	private Logger logger;

	public void save(MeetingEvent event) {
		repository.save(event);
		logger.info(String.format("Evento %s aggiunto in persistenza", event.getID()));

	}

	public void delete(MeetingEvent event) {
		repository.delete(event);
		logger.info(String.format("Evento %s cancellato", event.getID()));
	}
	
	public void update(MeetingEvent event) {
		if (findById(event.getID()) == null){
			throw new EntityNotFoundException(event.getID() + " not found");
		}
		
		repository.save(event);
	}
	
	
	public List<MeetingEvent> getEventsBetween(Date from, Date to) {
		try {
			List<MeetingEvent> events = repository.findAllByStartDateBetween(from, to);
			return events;
		} catch (Exception ex) {
			throw new MeetingEventException(ex.getMessage());
		}
	}

	public List<MeetingEvent> findAll(CalendarEventType type) {
		try {
			return repository.findAll();
		} catch (Exception ex) {
			throw new MeetingEventException(ex.getMessage());
		}
	}

	public List<MeetingEvent> findAllByCreator(String eventCreator) {
		try {
			return repository.findAllByEventCreator(eventCreator);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new MeetingEventException(ex.getMessage());
		}
	}

	public List<MeetingEvent> getEventsBetweenByCreator(Date from, Date to, String mailAziendaleCreator) {
		try {
			return repository.findAllByStartDateBetweenByCreator(from, to, mailAziendaleCreator);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new MeetingEventException(ex.getMessage());
		}
	}

	public MeetingEvent findById(String meetingId) {
		try {
			Optional<MeetingEvent> optionalEvent = repository.findById(meetingId);
			return optionalEvent.isPresent() ? optionalEvent.get() : null;
		} catch (Exception ex) {
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
		MeetingEvent event = findById(meetingId);
		if (event == null) {
			throw new EntityNotFoundException("Meeting non trovato");
		}
		List<EventContactDto> participants = event.getParticipants();

		Optional<EventContactDto> optionalParticipant = participants.stream()
				.filter(p -> p.getCodicePersona().equals(participantId)).findFirst();

		if (optionalParticipant.isEmpty()) {
			throw new EntityNotFoundException("Partecipante non trovato");
		}

		EventContactDto participant = optionalParticipant.get();
		participant.setParticipationStatus(status);
		participants.removeIf(p -> p.getCodicePersona().equals(participantId));
		participants.add(participant);
		event.setParticipants(participants);
		repository.save(event);
		logger.info(String.format("Evento %s aggiornato", event.getID()));
		return true;
	}
}
