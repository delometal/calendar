package com.perigea.tracker.calendar.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.HolidayRequestEvent;
import com.perigea.tracker.calendar.model.HolidayEvent;
import com.perigea.tracker.calendar.repository.HolidayEventRepository;
import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.CalendarEventType;
import com.perigea.tracker.commons.exception.EntityNotFoundException;
import com.perigea.tracker.commons.exception.HolidayEventException;

@Service
public class HolidayEventService {

	@Autowired
	private HolidayEventRepository repository;

	@Autowired
	private Logger logger;
	
	/**
	 * creazione di un evento di tipo ferie/permessi
	 * @param event
	 */
	public void save(HolidayRequestEvent event) {
		repository.save(event);
		logger.info(String.format("%s aggiunto in persistenza", event.getType()));

	}
	
	/**
	 * delete di un evento di tipo ferie/permessi
	 * @param event
	 */
	public void delete(HolidayRequestEvent event) {
		repository.delete(event);
		logger.info(String.format("%s rimosso", event.getType()));

	}
	
	/**
	 * update di un evento di tipo ferie/permessi
	 * @param event
	 */
	public void update(HolidayRequestEvent event) {
		if (repository.findById(event.getId()).isEmpty()) {
			throw new EntityNotFoundException(event.getId() + " not found");
		}

		repository.save(event);
	}
	
	/**
	 * lettura di tutti eventi di tipo ferie/permessi in base al richiedente
	 * @param mailAziendaleCreator
	 * @return
	 */
	public List<HolidayRequestEvent> findAllByEventCreator(String mailAziendaleCreator) {
		try {
			return repository.findAllByEventCreator(mailAziendaleCreator);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}
	
	/**
	 * lettura di tutti eventi di tipo ferie/permessi in base al responsabile che li deve approvare
	 * @param mailAziendaleResponsabile
	 * @return
	 */
	public List<HolidayRequestEvent> findAllByResponsabile(String mailAziendaleResponsabile) {
		try {
			return repository.findByResponsabile(mailAziendaleResponsabile);
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}
	
	/**
	 * lettura di tutti gli eventi in base al tipo 
	 * @param type
	 * @return
	 */
	public List<HolidayRequestEvent> findAllByType(CalendarEventType type) {
		try {
			return repository.findAllByType(type);
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}
	
	/**
	 * update dello status complessivo
	 * @param ID
	 * @param status
	 * @return
	 */
	public HolidayRequestEvent updateApprovalStatus(String ID, ApprovalStatus status) {
		try {
			HolidayRequestEvent event = findById(ID);
			event.setApproved(status);
			for (HolidayEvent e : event.getHolidays()) {
				e.setStatus(status);
			}

			repository.save(event);
			logger.info(String.format("Evento %s aggiornato", event.getType()));
			return event;
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}
	
	/**
	 * lettura di un evento di tipo ferie/permessi
	 * @param leaveId
	 * @return
	 */
	public HolidayRequestEvent findById(String leaveId) {
		try {
			Optional<HolidayRequestEvent> optionalEvent = repository.findById(leaveId);
			return optionalEvent.isPresent() ? optionalEvent.get() : null;
		} catch (Exception ex) {
			if (ex instanceof NoSuchElementException) {
				throw new EntityNotFoundException(ex.getMessage());
			}
			throw new HolidayEventException(ex.getMessage());
		}
	}
	
	/**
	 * metodo di approvazione dei singoli giorni di ferie/permesso
	 * @param list
	 * @param id
	 * @return
	 */
	public HolidayRequestEvent approveSingleEvent(List<HolidayEvent> list, String id) {
		try {
			HolidayRequestEvent event = findById(id);
			if (event != null) {
				event.setHolidays(list);
				for (HolidayEvent e : list) {
					if (e.getStatus().equals(ApprovalStatus.DECLINED)) {
						event.setApproved(ApprovalStatus.DECLINED);
						break;
					}
				}
			}
			return repository.save(event);
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}

	/**
	 * delete dei singoli giorni di ferie/permesso
	 * @param list
	 * @param id
	 * @return
	 */
	public HolidayRequestEvent deleteSingleHolidayEvents(List<HolidayEvent> list, String id) {
		try {
			HolidayRequestEvent event = findById(id);
			if (event != null) {
				for (HolidayEvent e : list) {
					if (event.getHolidays().add(e)) {
						event.getHolidays().remove(e);
					}
				}
			}
			return repository.save(event);
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}
		

	
//	public List<HolidayRequestEvent> findAllByDateCreatorType(Date from, Date to, String mailAziendaleCreator,
//			CalendarEventType type) {
//		try {
//			return repository.findAllByStartDateBetweenByCreatorByType(from, to, mailAziendaleCreator, type);
//		} catch (Exception ex) {
//			if (ex instanceof NoSuchElementException) {
//				throw new EntityNotFoundException(ex.getMessage());
//			}
//			throw new HolidayEventException(ex.getMessage());
//		}
//	}

//	public List<HolidayRequestEvent> findAllByDateResponsabileType(Date from, Date to, String mailAziendaleResponsabile,
//			CalendarEventType type) {
//		try {
//			return repository.findAllByStartDateBetweenByResponsabileByType(from, to, mailAziendaleResponsabile, type);
//		} catch (Exception ex) {
//			if (ex instanceof NoSuchElementException) {
//				throw new EntityNotFoundException(ex.getMessage());
//			}
//			throw new HolidayEventException(ex.getMessage());
//		}
//	}

//	public List<HolidayRequestEvent> getEventsBetween(Date from, Date to) {
//		try {
//			return repository.findAllByStartDateBetween(from, to);
//		} catch (Exception ex) {
//			throw new HolidayEventException(ex.getMessage());
//		}
//	}

}
