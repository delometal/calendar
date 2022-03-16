package com.perigea.tracker.calendar.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.TimesheetEvent;
import com.perigea.tracker.calendar.repository.TimesheetEventRepository;
import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.HolidayEventException;
import com.perigea.tracker.commons.exception.TimesheetEventException;

@Service
public class TimesheetEventService {

	@Autowired
	private Logger logger;

	@Autowired
	private TimesheetEventRepository timesheetRepository;
	
	/**
	 * create 
	 * @param event
	 */
	public void save(TimesheetEvent event) {
		timesheetRepository.save(event);
		logger.info(String.format("%s aggiunto in persistenza", event.getType()));

	}
	
	/**
	 * delete
	 * @param event
	 */
	public void delete(TimesheetEvent event) {
		timesheetRepository.delete(event);
		logger.info(String.format("%s rimosso", event.getType()));

	}
	
	/**
	 * update
	 * @param event
	 */
	public void update(TimesheetEvent event) {
		if (timesheetRepository.findById(event.getId()).isEmpty()) {
			throw new EntityNotFoundException(event.getId() + " not found");
		}
		System.out.println(timesheetRepository.findById(event.getId()));
		timesheetRepository.save(event);
	}
	
	/**
	 * lettura di tutti gli eventi in base al creator
	 * @param mailAziendaleCreator
	 * @return
	 */
	public List<TimesheetEvent> findAllByEventCreator(String mailAziendaleCreator) {
		try {
			return timesheetRepository.findAllByEventCreator(mailAziendaleCreator);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new TimesheetEventException(ex.getMessage());
		}
	}
	
	/**
	 * aggiornamento dello stato di approvazione di un timesheet
	 * @param ID
	 * @param status
	 * @return
	 */
	public TimesheetEvent updateApprovalStatus(String ID, ApprovalStatus status) {
		try {
			TimesheetEvent event = findById(ID);
			event.setApprovalStatus(status);
			timesheetRepository.save(event);
			logger.info(String.format("Evento %s aggiornato", event.getType()));
			return event;
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}
	
	/**
	 * lettura di un evento in base all'id
	 * @param ID
	 * @return
	 */
	public TimesheetEvent findById(String ID) {
		try {
			Optional<TimesheetEvent> optionalEvent = timesheetRepository.findById(ID);
			return optionalEvent.isPresent() ? optionalEvent.get() : null;
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new TimesheetEventException(ex.getMessage());
		}
	}

}
