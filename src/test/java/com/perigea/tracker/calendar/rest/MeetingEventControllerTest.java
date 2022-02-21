package com.perigea.tracker.calendar.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perigea.tracker.calendar.entity.Contact;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.mapper.MeetingMapper;
import com.perigea.tracker.calendar.repository.MeetingEventRepository;
import com.perigea.tracker.calendar.service.EmailBuilderService;
import com.perigea.tracker.calendar.service.MeetingEventService;
import com.perigea.tracker.calendar.service.MeetingRoomService;
import com.perigea.tracker.calendar.service.SchedulerService;
import com.perigea.tracker.commons.enums.CalendarEventType;

//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})


@RunWith(SpringRunner.class)
@WebMvcTest(MeetingEventController.class)
public class MeetingEventControllerTest {

	@Autowired
	private MockMvc mvc;
	
//	@MockBean 
//	private MeetingEventService service;
//	
	@MockBean
	private MeetingEventRepository repository;
	
	@MockBean
	private MeetingEventService mockService;
	@MockBean
	private SchedulerService schedulerService;
	@MockBean
	private EmailBuilderService eb;
	@MockBean
	private NotificationRestClient cl;
	@MockBean
	private MeetingRoomService rs;
	@MockBean
	private MeetingMapper mp;
	@MockBean
	private RestTemplate rest;
	
	
	private static MeetingEvent event1;
	private static MeetingEvent event2;
	private static Contact contact;
	private static Contact secondContact;
	private static List<MeetingEvent> events = new ArrayList<>();
	
	@BeforeAll
	public static void dataSetup() {
		contact = new Contact();
		event1 = new MeetingEvent();
		event2 = new MeetingEvent();
		secondContact = new Contact();
		
		contact.setMailAziendale("pippo@perigea.it");
		contact.setCodicePersona("UniqueID");
		
		secondContact.setMailAziendale("nobody@nothing.it");
		secondContact.setCodicePersona("MoreUniqueID");
		
		event1.setEventCreator(contact);
		event1.setType(CalendarEventType.Riunione);
		event1.setId("VeryUniqueID");
		
		event2.setEventCreator(secondContact);
		event2.setType(CalendarEventType.Riunione);
		event2.setId("randomID");
		
		events.add(event1);
		events.add(event2);
	}
	
	 public static String asJsonString(final Object obj) {
	        try {
	            return new ObjectMapper().writeValueAsString(obj);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	@Test
	public void saveEventTest() throws Exception{
		String id = "VeryUniqueID";
		doNothing().when(mockService).save(event1);
		
		 this.mvc.perform(post("/meeting/create-meeting")
	        		.content(asJsonString(event1))
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.MeetingEvent.Id", is(id)));
	}
	
}
