package com.perigea.tracker.calendar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.perigea.tracker.calendar.entity.Contact;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.commons.enums.CalendarEventType;

//TODO Utility class da estendere per test

@DataMongoTest
@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {MongoConfig.class})
public class MeetingEventRepositoryTestEmbedded {

//	@Autowired
//	MongoTemplate template;
	
	@Autowired
	private MeetingEventRepository repository;
	private Contact contact;
	private Contact secondContact;
	private MeetingEvent event;
	private MeetingEvent secondEvent;
	private static final Integer TOTAL = 2;
	
	@BeforeEach
	public void dataSetup() {
		contact = new Contact();
		event = new MeetingEvent();
		secondEvent = new MeetingEvent();
		secondContact = new Contact();
		
		contact.setMailAziendale("pippo@perigea.it");
		contact.setCodicePersona("UniqueID");
		
		secondContact.setMailAziendale("nobody@nothing.it");
		secondContact.setCodicePersona("MoreUniqueId");
		
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
