package com.perigea.tracker.calendar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.ArchivedEvent;
import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.calendar.repository.ArchivedEventRepository;
import com.perigea.tracker.commons.enums.EventStatus;

@Service
@EnableScheduling
public class ArchivingService {

	@Autowired
	private ScheduledEventRepositoryService repositoryService;

	@Autowired
	private ArchivedEventRepository archivedRepo;
	
	/**
	 * metodo per l'archiviazione di eventi in precedenza schedulati ed ora inattivi
	 */
	@Scheduled(cron = "* 0 0 1 1 *")
	public void archiveInactiveScheduleEvent() {
		List<ScheduledEvent> inactiveScheduledEvents = repositoryService.getAllByStatus(EventStatus.INACTIVE);
		for (ScheduledEvent event : inactiveScheduledEvents) {
			ArchivedEvent archive = new ArchivedEvent();
			archive.setId(event.getId());
			archive.setInactiveEvent(event);
			archivedRepo.save(archive);
		}
		repositoryService.deleteAll(inactiveScheduledEvents);
	}
}
