package com.perigea.tracker.calendar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import com.perigea.tracker.calendar.configuration.MongoConfig;
import com.perigea.tracker.calendar.entity.Contact;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.commons.enums.CalendarEventType;

@DataMongoTest
@ContextConfiguration(classes = {MongoConfig.class})
public class MeetingEventRepositoryTest {
	
	@Autowired
	private MeetingEventRepository repository;
	
	private static Contact contact;
	private static Contact secondContact;
	private static MeetingEvent event;
	private static MeetingEvent secondEvent;
	private static final Integer TOTAL = 2;
	
	@BeforeAll
	public static void dataSetup(@Autowired MeetingEventRepository repository) {
		contact = new Contact();
		event = new MeetingEvent();
		secondEvent = new MeetingEvent();
		secondContact = new Contact();
		
		contact.setMailAziendale("pippo@perigea.it");
		contact.setCodicePersona("UniqueID");
		
		secondContact.setMailAziendale("nobody@nothing.it");
		secondContact.setCodicePersona("MoreUniqueID");
		
		event.setEventCreator(contact);
		event.setType(CalendarEventType.Riunione);
		event.setId("VeryUniqueID");
		
		secondEvent.setEventCreator(secondContact);
		secondEvent.setType(CalendarEventType.Riunione);
		secondEvent.setId("randomID");
		
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
	
	@Test
	public void deleteTest() {
		repository.delete(event);
		List<MeetingEvent> all = repository.findAll();
		assertThat(all.size()).isEqualTo(TOTAL - 1);
	}

}
