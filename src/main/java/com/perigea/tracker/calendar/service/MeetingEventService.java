package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.Contact;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.repository.MeetingEventRepository;
import com.perigea.tracker.commons.enums.ParticipationStatus;
import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.MeetingEventException;
import com.perigea.tracker.commons.exception.MeetingRoomReservedException;
import com.perigea.tracker.commons.utils.Utils;

@Service
public class MeetingEventService {

	@Autowired
	private MeetingEventRepository repository;

	@Autowired
	private Logger logger;

	public void save(MeetingEvent event) {
		if(isReserved(event)) {
			throw new MeetingRoomReservedException("Stanza nel periodo indicato non disponibile!");
		}
		repository.save(event);
		logger.info(String.format("Evento %s aggiunto in persistenza", event.getId()));

	}

	public void delete(MeetingEvent event) {
		repository.delete(event);
		logger.info(String.format("Evento %s cancellato", event.getId()));
	}
	
	public void update(MeetingEvent event) {
		if (findById(event.getId()) == null){
			throw new EntityNotFoundException(event.getId() + " not found");
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

	public List<MeetingEvent> findAll() {
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
		List<Contact> participants = event.getParticipants();

		Optional<Contact> optionalParticipant = participants.stream()
				.filter(p -> p.getCodicePersona().equals(participantId)).findFirst();

		if (optionalParticipant.isEmpty()) {
			throw new EntityNotFoundException("Partecipante non trovato");
		}

		Contact participant = optionalParticipant.get();
		participant.setParticipationStatus(status);
		participants.removeIf(p -> p.getCodicePersona().equals(participantId));
		participants.add(participant);
		event.setParticipants(participants);
		repository.save(event);
		logger.info(String.format("Evento %s aggiornato", event.getId()));
		return true;
	}
	

	private boolean isReserved(MeetingEvent event) {
		
		Date startDate = Utils.shiftTime(event.getStartDate(), -1);
		Date endDate = Utils.shiftTime(event.getEndDate(), +1);;
		
		
		List<MeetingEvent> alreadyReserved = repository.blockingMeetingsInRange(startDate, endDate);
		
		if(alreadyReserved.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
}
