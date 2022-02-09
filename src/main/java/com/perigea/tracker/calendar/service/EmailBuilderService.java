package com.perigea.tracker.calendar.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static final String PATTERN = "yyyy-MM-dd HH:mm";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN);
	
	public Email buildFromMeetingEvent(MeetingEvent event, String azione) {
		List<String> recipients = new ArrayList<>();
		for (EventContactDto c : event.getParticipants()) {
			recipients.add(c.getMailAziendale());
		}	
		Map<String, Object> templateData = new HashMap<>();		
		templateData.put("creator", event.getEventCreator().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("data", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("partecipanti", recipients);
		templateData.put("azione", azione);
		
		return Email.builder()
				.from(sender)
				.templateName("meetingTemplate.ftlh")
				.templateModel(templateData)
				.subject(event.getDescription())
				.emailType(EmailType.HTML_TEMPLATE_MAIL)
				.to(recipients)
				.build();
	}
	
	public Email buildFromLeaveEvent(LeaveEvent event) {
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getResponsabile().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("richiedente", event.getEventCreator().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("data", DATE_FORMAT.format(event.getStartDate()));
		
		return Email.builder()
				.from(sender)
				.templateName("leaveTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s richiesta da %s",event.getType().toString(), event.getEventCreator().getMailAziendale()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL)
				.to(recipient)
				.build();		
	}

}


