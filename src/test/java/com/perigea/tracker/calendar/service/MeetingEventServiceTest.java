package com.perigea.tracker.calendar.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.model.Contact;
import com.perigea.tracker.calendar.repository.MeetingEventRepository;
import com.perigea.tracker.commons.enums.CalendarEventType;

@ExtendWith(MockitoExtension.class)
class MeetingEventServiceTest {

	@Mock
	private	MeetingEventRepository repository;
	@Mock
	private Logger logger;
	@InjectMocks
	private MeetingEventService service;
	private static Contact contact;
	private static Contact secondContact;
	private static MeetingEvent event;
	private static MeetingEvent secondEvent;
	private static List<MeetingEvent> events = new ArrayList<>();
	private static List<MeetingEvent> pippoEvents = new ArrayList<>();

	
	@BeforeAll
	public static void init() {
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
		
		events.add(event);
		pippoEvents.add(event);
		events.add(secondEvent);
	}
	
	@Test
	public void findByIdTest() {
		when(repository.findById("VeryUniqueID")).thenReturn(Optional.of(event));
		assertEquals(service.findById("VeryUniqueID"), event);
	}
	
	@Test
	public void findAllTest() {
		when(repository.findAll()).thenReturn(events);
		assertEquals(service.findAll(), events);
	}
	
	@Test
	public void findAllByCreatorTest() {
		when(repository.findAllByEventCreator("pippo@perigea.it")).thenReturn(pippoEvents);
		assertEquals(repository.findAllByEventCreator("pippo@perigea.it"), pippoEvents);
	}
}
