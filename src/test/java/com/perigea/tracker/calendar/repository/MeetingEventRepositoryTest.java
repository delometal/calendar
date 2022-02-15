package com.perigea.tracker.calendar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import com.perigea.tracker.calendar.configuration.MongoConfig;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.commons.dto.EventContactDto;
import com.perigea.tracker.commons.enums.CalendarEventType;

@DataMongoTest
@ComponentScan(basePackages = {
		"com.perigea.tracker.calendar.configuration",
		})
@ContextConfiguration(classes = {MongoConfig.class})
public class MeetingEventRepositoryTest {
	
	@Autowired
	private MeetingEventRepository repository;
	private EventContactDto contact;
	private EventContactDto secondContact;
	private MeetingEvent event;
	private MeetingEvent secondEvent;
	private static final Integer TOTAL = 2;
	
	@BeforeEach
	public void dataSetup() {
		contact = new EventContactDto();
		event = new MeetingEvent();
		secondEvent = new MeetingEvent();
		secondContact = new EventContactDto();
		
		contact.setMailAziendale("pippo@perigea.it");
		contact.setId(123L);
		
		secondContact.setMailAziendale("nobody@nothing.it");
		secondContact.setId(456L);
		
		event.setEventCreator(contact);
		event.setType(CalendarEventType.Riunione);
		event.setID("VeryUniqueID");
		
		secondEvent.setEventCreator(secondContact);
		secondEvent.setType(CalendarEventType.Riunione);
		secondEvent.setID("randomID");
		
		repository.save(event);
		repository.save(secondEvent);
	}
	
	@Test
	public void findByEventIdTest() {
		String id = "VeryUniqueID";
		MeetingEvent result = repository.findById(id).get();
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void findByCreatorTest() {
		String creator = contact.getMailAziendale();
		List<MeetingEvent> result = repository.findAllByEventCreator(creator);
		assertThat(result.get(0)).isNotNull();
		assertThat(result.get(0)).isEqualTo(event);
	}
	
	@Test
	public void findAllTest() {
		List<MeetingEvent> all = repository.findAll();
		assertThat(all.size()).isEqualTo(TOTAL);
	}

}
