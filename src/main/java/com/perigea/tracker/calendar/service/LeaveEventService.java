package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.dto.ContactDto;
import com.perigea.tracker.calendar.dto.LeaveEventDto;
import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.calendar.enums.ApprovalStatus;
import com.perigea.tracker.calendar.enums.CalendarEventType;
import com.perigea.tracker.calendar.exception.EntityNotFoundException;
import com.perigea.tracker.calendar.exception.LeaveEventException;
import com.perigea.tracker.calendar.mapper.LeaveMapper;
import com.perigea.tracker.calendar.repository.LeaveEventRepository;

@Service
public class LeaveEventService {// implements EventServiceStrategy<LeaveEvent> {

	@Autowired
	private LeaveEventRepository repository;
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private LeaveMapper mapper;

	public LeaveEventDto save(LeaveEvent event) {
		LeaveEventDto savedEvent = mapper.mapToDto(repository.save(event));
		logger.info(String.format("%s aggiunto in persistenza", event.getType()));
		return savedEvent;
	}

	public void delete(LeaveEvent event) {
		repository.delete(event);
		logger.info(String.format("%s rimosso", event.getType()));

	}

	public List<LeaveEventDto> getEventsBetween(Date from, Date to) {
		try {
			return mapper.mapToDtoList(repository.findAllByStartDateBetween(from, to));
		} catch (Exception ex) {
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEventDto> findAllByEventCreator(ContactDto creator) {
		try {
			return mapper.mapToDtoList(repository.findAllByEventCreator(creator));
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEventDto> findAllByType(CalendarEventType type) {
		try {
			return mapper.mapToDtoList(repository.findAllByType(type));
		} catch (Exception ex) {
			throw new LeaveEventException(ex.getMessage());
		}
	}
	
	public List<LeaveEventDto> findAllByDateCreatorType(Date from, Date to, ContactDto creator, CalendarEventType type) {
		try {
			return mapper.mapToDtoList(
					repository.findAllByStartDateBetweenByCreatorByType(from, to, creator, type));
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}
	
	public List<LeaveEventDto> findAllByDateCreatorListType(Date from, Date to, List<ContactDto> creators, CalendarEventType type) {
		try {
			return mapper.mapToDtoList(
					repository.findAllByStartDateBetweenByCreatorListByType(from, to, creators, type));
		} catch (Exception ex) {
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public LeaveEventDto updateApprovalStatus(String ID, ApprovalStatus status) {
		try {
			LeaveEventDto event = findById(ID);
			event.setApproved(status);
			repository.save(mapper.mapToEntity(event));
			logger.info(String.format("Evento %s aggiornato", event.getType()));
			return event;
		} catch (Exception ex) {
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public LeaveEventDto findById(String leaveId) {
		try {
			Optional<LeaveEvent> optionalEvent = repository.findById(leaveId);
			return optionalEvent.isPresent() ? mapper.mapToDto(optionalEvent.get()) : null;
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}
}
