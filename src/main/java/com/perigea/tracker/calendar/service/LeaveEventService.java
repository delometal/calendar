package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.calendar.repository.LeaveEventRepository;
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

	public void save(LeaveEvent event) {
		repository.save(event);
		logger.info(String.format("%s aggiunto in persistenza", event.getType()));

	}

	public void delete(LeaveEvent event) {
		repository.delete(event);
		logger.info(String.format("%s rimosso", event.getType()));

	}

	public List<LeaveEvent> getEventsBetween(Date from, Date to) {
		try {
			return repository.findAllByStartDateBetween(from, to);
		} catch (Exception ex) {
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEvent> findAllByEventCreator(String mailAziendaleCreator) {
		try {
			return repository.findAllByEventCreator(mailAziendaleCreator);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEvent> findAllByResponsabile(String mailAziendaleResponsabile) {
		try {
			return repository.findByResponsabile(mailAziendaleResponsabile);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEvent> findAllByType(CalendarEventType type) {
		try {
			return repository.findAllByType(type);
		} catch (Exception ex) {
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEvent> findAllByDateCreatorType(Date from, Date to, String mailAziendaleCreator,
			CalendarEventType type) {
		try {
			return repository.findAllByStartDateBetweenByCreatorByType(from, to, mailAziendaleCreator, type);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public List<LeaveEvent> findAllByDateResponsabileType(Date from, Date to, String mailAziendaleResponsabile,
			CalendarEventType type) {
		try {
			return repository.findAllByStartDateBetweenByResponsabileByType(from, to, mailAziendaleResponsabile, type);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public LeaveEvent updateApprovalStatus(String ID, ApprovalStatus status) {
		try {
			LeaveEvent event = findById(ID);
			event.setApproved(status);
			repository.save(event);
			logger.info(String.format("Evento %s aggiornato", event.getType()));
			return event;
		} catch (Exception ex) {
			throw new LeaveEventException(ex.getMessage());
		}
	}

	public LeaveEvent findById(String leaveId) {
		try {
			Optional<LeaveEvent> optionalEvent = repository.findById(leaveId);
			return optionalEvent.isPresent() ? optionalEvent.get() : null;
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new LeaveEventException(ex.getMessage());
		}
	}
}
