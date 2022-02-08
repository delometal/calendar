package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.calendar.mapper.LeaveMapper;
import com.perigea.tracker.calendar.repository.LeaveEventRepository;
import com.perigea.tracker.commons.dto.LeaveEventDto;
import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.LeaveEventException;

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

	public List<LeaveEventDto> findAllByEventCreator(String mailAziendaleCreator) {
		try {
			return mapper.mapToDtoList(repository.findAllByEventCreator(mailAziendaleCreator));
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEventDto> findAllByResponsabile(String mailAziendaleResponsabile) {
		try {
			return mapper.mapToDtoList(repository.findByResponsabile(mailAziendaleResponsabile));
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

	public List<LeaveEventDto> findAllByDateCreatorType(Date from, Date to, String mailAziendaleCreator,
			CalendarEventType type) {
		try {
			return mapper.mapToDtoList(
					repository.findAllByStartDateBetweenByCreatorByType(from, to, mailAziendaleCreator, type));
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEventDto> findAllByDateResponsabileType(Date from, Date to, String mailAziendaleResponsabile,
			CalendarEventType type) {
		try {
			return mapper.mapToDtoList(
					repository.findAllByStartDateBetweenByResponsabileByType(from, to, mailAziendaleResponsabile, type));
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
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
