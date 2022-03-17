package com.perigea.tracker.calendar.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.model.Contact;
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
	
	/**
	 * creazione di un meeting
	 * @param event
	 */
	public void save(MeetingEvent event) {
		if(isReserved(event)) {
			throw new MeetingRoomReservedException("Stanza nel periodo indicato non disponibile!");
		}
		repository.save(event);
		logger.info(String.format("Evento %s aggiunto in persistenza", event.getId()));

	}
	
	/**
	 * delete di un meeting
	 * @param event
	 */
	public void delete(MeetingEvent event) {
		repository.delete(event);
		logger.info(String.format("Evento %s cancellato", event.getId()));
	}
	
	/**
	 * update di un meeting
	 * @param event
	 */
	public void update(MeetingEvent event) {
		if (findById(event.getId()) == null){
			throw new EntityNotFoundException(event.getId() + " not found");
		}
		
		repository.save(event);
	}
	
	/**
	 * lettura di tutti i meeting tra due date
	 * @param from
	 * @param to
	 * @return
	 */
	public List<MeetingEvent> getEventsBetween(LocalDateTime from, LocalDateTime to) {
		try {
			List<MeetingEvent> events = repository.findAllByStartDateBetween(from, to);
			return events;
		} catch (Exception ex) {
			throw new MeetingEventException(ex.getMessage());
		}
	}
	
	/**
	 * lettura di tutti i meeting
	 * @return
	 */
	public List<MeetingEvent> findAll() {
		try {
			return repository.findAll();
		} catch (Exception ex) {
			throw new MeetingEventException(ex.getMessage());
		}
	}
	
	/**
	 * lettura di tutti i meeting in base al creator
	 * @param eventCreator
	 * @return
	 */
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
	
	/**
	 * lettura di tutti i meeting in base al creator e tra 2 date
	 * @param from
	 * @param to
	 * @param mailAziendaleCreator
	 * @return
	 */
	public List<MeetingEvent> getEventsBetweenByCreator(LocalDateTime from, LocalDateTime to, String mailAziendaleCreator) {
		try {
			return repository.findAllByStartDateBetweenByCreator(from, to, mailAziendaleCreator);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new MeetingEventException(ex.getMessage());
		}
	}
	
	/**
	 * lettura di un meeting in base all'id
	 * @param meetingId
	 * @return
	 */
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
	
	/**
	 * metodo per l'accettazione dell'invito di partecipazione ad un meeting
	 * @param meetingId
	 * @param participantId
	 * @return
	 */
	public boolean acceptInvite(String meetingId, String participantId) {
		return changeInviteStatus(meetingId, participantId, ParticipationStatus.CONFERMATA);
	}
	
	/**
	 * metodo per il rifiuto dell'invito di partecipazione ad un meeting
	 * @param meetingId
	 * @param participantId
	 * @return
	 */
	public boolean declineInvite(String meetingId, String participantId) {
		return changeInviteStatus(meetingId, participantId, ParticipationStatus.NON_CONFERMATA);
	}
	
	/**
	 * aggiornamento dello status di partecipazione ad un evento
	 * @param meetingId
	 * @param participantId
	 * @param status
	 * @return
	 */
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
		
	/**
	 * metodo per il controllo della disponibilità della sala riunioni
	 * @param event
	 * @return
	 */
	private boolean isReserved(MeetingEvent event) {
		
		LocalDateTime startDate = Utils.shiftLocalDateTime(event.getStartDate(), -1);
		LocalDateTime endDate = Utils.shiftLocalDateTime(event.getEndDate(), 1);
		
		
		List<MeetingEvent> alreadyReserved = repository.blockingMeetingsInRange(startDate, endDate);
		
		return !alreadyReserved.isEmpty();
		
	}
}
