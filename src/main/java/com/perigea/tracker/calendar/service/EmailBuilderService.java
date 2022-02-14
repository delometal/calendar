package com.perigea.tracker.calendar.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.commons.dto.EventContactDto;
import com.perigea.tracker.commons.enums.EmailType;
import com.perigea.tracker.commons.model.Email;

@Service
public class EmailBuilderService {
	
	@Value("${spring.mail.username}")
	private String sender;
	
	private static final String PATTERN = "dd-MM-yyyy HH:mm";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN);
	
	
	public Email buildFromMeetingEvent(MeetingEvent event, String azione) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipients = new ArrayList<>();
		for (EventContactDto c : event.getParticipants()) {
			recipients.add(c.getMailAziendale());
		}	
		
		Map<String, Object> templateData = new HashMap<>();		
		templateData.put("creator", String.format("%s %s", 
				event.getEventCreator().getNome(),
				event.getEventCreator().getCognome()));
		templateData.put("eventType", event.getType());
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("dataFine", DATE_FORMAT.format(event.getEndDate()));
		templateData.put("partecipanti", recipients);
		templateData.put("azione", azione);
		templateData.put("presenza", event.isInPerson());
		
		return Email.builder()
				.eventID(event.getID())
				.from(sender)
				.templateName("meetingTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s: %s", 
						event.getType(), 
						event.getDescription()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL)
				.to(recipients)
				.build();
	}
	
	public Email buildReminderEmail(MeetingEvent event) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipients = new ArrayList<>();
		for (EventContactDto c : event.getParticipants()) {
			recipients.add(c.getMailAziendale());
		}	

		Map<String, Object> templateData = new HashMap<>();		
		templateData.put("creator", String.format("%s %s", 
				event.getEventCreator().getNome(),
				event.getEventCreator().getCognome()));
		templateData.put("eventType", event.getType());
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("partecipanti", recipients);
		
		return Email.builder()
				.eventID(event.getID())
				.from(sender)
				.templateName("notificationTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("REMINDER: %s inizierà a breve", 
						event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL)
				.to(recipients)
				.build();
		
	}
	
	public Email buildFromLeaveEvent(LeaveEvent event, String azione) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getResponsabile().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getEventCreator().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", azione);
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("dataFine", DATE_FORMAT.format(event.getEndDate()));
		
		return Email.builder()
				.eventID(event.getID())
				.from(sender)
				.templateName("leaveTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s %s: %s",
						event.getEventCreator().getNome(), 
						event.getEventCreator().getCognome(), 
						event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL)
				.to(recipient)
				.build();		
	}
	
	public Email buildApprovalEmail(LeaveEvent event) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getEventCreator().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getResponsabile().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", event.getApproved().toString().toLowerCase());
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("dataFine", DATE_FORMAT.format(event.getEndDate()));
		
		return Email.builder()
				.eventID(event.getID())
				.from(sender)
				.templateName("leaveTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s %s: %s %s",
						event.getResponsabile().getNome(), 
						event.getResponsabile().getCognome(), 
						event.getType(),
						event.getApproved().toString().toLowerCase()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL)
				.to(recipient)
				.build();		
	}

}


