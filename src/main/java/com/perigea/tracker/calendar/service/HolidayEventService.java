package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.HolidayEvent;
import com.perigea.tracker.calendar.repository.HolidayEventRepository;
import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.HolidayEventException;

@Service
public class HolidayEventService {// implements EventServiceStrategy<HolidayEvent> {

	@Autowired
	private HolidayEventRepository repository;

	@Autowired
	private Logger logger;

	public void save(HolidayEvent event) {
		repository.save(event);
		logger.info(String.format("%s aggiunto in persistenza", event.getType()));

	}

	public void delete(HolidayEvent event) {
		repository.delete(event);
		logger.info(String.format("%s rimosso", event.getType()));

	}
	
	public void update(HolidayEvent event) {
		if (repository.findById(event.getId()).isEmpty()) {
			throw new EntityNotFoundException(event.getId() + " not found");
		}
		System.out.println(repository.findById(event.getId()));
		repository.save(event);
	}

	public List<HolidayEvent> getEventsBetween(Date from, Date to) {
		try {
			return repository.findAllByStartDateBetween(from, to);
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public List<HolidayEvent> findAllByEventCreator(String mailAziendaleCreator) {
		try {
			return repository.findAllByEventCreator(mailAziendaleCreator);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public List<HolidayEvent> findAllByResponsabile(String mailAziendaleResponsabile) {
		try {
			return repository.findByResponsabile(mailAziendaleResponsabile);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public List<HolidayEvent> findAllByType(CalendarEventType type) {
		try {
			return repository.findAllByType(type);
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public List<HolidayEvent> findAllByDateCreatorType(Date from, Date to, String mailAziendaleCreator,
			CalendarEventType type) {
		try {
			return repository.findAllByStartDateBetweenByCreatorByType(from, to, mailAziendaleCreator, type);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public List<HolidayEvent> findAllByDateResponsabileType(Date from, Date to, String mailAziendaleResponsabile,
			CalendarEventType type) {
		try {
			return repository.findAllByStartDateBetweenByResponsabileByType(from, to, mailAziendaleResponsabile, type);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public HolidayEvent updateApprovalStatus(String ID, ApprovalStatus status) {
		try {
			HolidayEvent event = findById(ID);
			event.setApproved(status);
			repository.save(event);
			logger.info(String.format("Evento %s aggiornato", event.getType()));
			return event;
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public HolidayEvent findById(String leaveId) {
		try {
			Optional<HolidayEvent> optionalEvent = repository.findById(leaveId);
			return optionalEvent.isPresent() ? optionalEvent.get() : null;
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}
}
