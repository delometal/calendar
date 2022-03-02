package com.perigea.tracker.calendar.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.Contact;
import com.perigea.tracker.calendar.entity.HolidayEvent;
import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.calendar.entity.TimesheetEvent;
import com.perigea.tracker.calendar.factory.ICSFactory;
import com.perigea.tracker.commons.dto.AttachmentDto;
import com.perigea.tracker.commons.enums.EMese;
import com.perigea.tracker.commons.enums.EmailType;
import com.perigea.tracker.commons.exception.NullFieldException;
import com.perigea.tracker.commons.model.Email;
import com.perigea.tracker.commons.utils.NotNullValidator;

@Service
public class EventEmailBuilderService {

	@Value("${spring.mail.username}")
	private String sender;

	private static final String PATTERN = "dd-MM-yyyy HH:mm";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN);

	public Email build(MeetingEvent event, String azione) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipients = new ArrayList<>();
		Map<String, Object> templateData = new HashMap<>();
		List<AttachmentDto> attachments = new ArrayList<AttachmentDto>();
	
		// TODO elenco campi null
		if (!NotNullValidator.validate(event))
			throw new NullFieldException(String.format("%s must not be null!", NotNullValidator.getDetails(event)));
		
		for (Contact c : event.getParticipants()) {
			recipients.add(c.getMailAziendale());
		}
		//attachments = addAttachments(list);
		attachments.add(addICSFile(event));
		templateData.put("creator",
		String.format("%s %s", event.getEventCreator().getNome(), event.getEventCreator().getCognome()));
		templateData.put("eventType", event.getType());
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("dataFine", DATE_FORMAT.format(event.getEndDate()));
		templateData.put("partecipanti", recipients);
		templateData.put("azione", azione);
		templateData.put("presenza", event.getInPerson().booleanValue());
		
		return Email.builder().eventID(event.getId()).from(sender).templateName("meetingTemplate.ftlh")
				.templateModel(templateData).subject(String.format("%s: %s", event.getType(), event.getDescription()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipients).attachments(attachments).build();
	}

	public Email buildReminder(MeetingEvent event) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipients = new ArrayList<>();
		for (Contact c : event.getParticipants()) {
			recipients.add(c.getMailAziendale());
		}
		
		List<AttachmentDto> attachments = new ArrayList<AttachmentDto>();
		attachments.add(addICSFile(event));

		Map<String, Object> templateData = new HashMap<>();
		templateData.put("creator",
				String.format("%s %s", event.getEventCreator().getNome(), event.getEventCreator().getCognome()));
		templateData.put("eventType", event.getType());
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("partecipanti", recipients);

		return Email.builder().eventID(event.getId()).from(sender).templateName("notificationTemplate.ftlh")
				.templateModel(templateData).subject(String.format("REMINDER: %s inizierà a breve", event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipients).attachments(attachments).build();

	}
	
	public Email build(TimesheetEvent event, String azione) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		EMese mese = EMese.getByMonthId(event.getTimesheet().getMese());
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getResponsabile().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getEventCreator().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", azione);
		templateData.put("mese", mese.getDescription());
		templateData.put("anno", event.getTimesheet().getAnno());
		
		return Email.builder().eventID(event.getId()).from(sender).templateName("timesheetTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s %s: %s", event.getEventCreator().getNome(),
						event.getEventCreator().getCognome(), event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}

	public Email build(HolidayEvent event, String azione) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getResponsabile().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getEventCreator().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", azione);
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("dataFine", DATE_FORMAT.format(event.getEndDate()));

		return Email.builder().eventID(event.getId()).from(sender).templateName("leaveTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s %s: %s", event.getEventCreator().getNome(),
						event.getEventCreator().getCognome(), event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}
	
	public Email buildApproval(TimesheetEvent event) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		EMese mese = EMese.getByMonthId(event.getTimesheet().getMese());
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getEventCreator().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getResponsabile().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", event.getApprovalStatus().toString().toLowerCase());
		templateData.put("mese", mese.getDescription());
		templateData.put("anno", event.getTimesheet().getAnno());
		
		return Email.builder().eventID(event.getId()).from(sender).templateName("timesheetTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s %s: %s %s", event.getResponsabile().getNome(),
						event.getResponsabile().getCognome(), event.getType(),
						event.getApprovalStatus().toString().toLowerCase()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}

	public Email buildApproval(HolidayEvent event) {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getEventCreator().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getResponsabile().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", event.getApproved().toString().toLowerCase());
		templateData.put("dataInizio", DATE_FORMAT.format(event.getStartDate()));
		templateData.put("dataFine", DATE_FORMAT.format(event.getEndDate()));

		return Email.builder().eventID(event.getId()).from(sender).templateName("leaveTemplate.ftlh")
				.templateModel(templateData)
				.subject(String.format("%s %s: %s %s", event.getResponsabile().getNome(),
						event.getResponsabile().getCognome(), event.getType(),
						event.getApproved().toString().toLowerCase()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}
	
	public AttachmentDto addICSFile(MeetingEvent event) {
		AttachmentDto attch = new AttachmentDto();
		attch.setBArray(ICSFactory.createICS(event));
		attch.setFileName("text/calendar");
		return attch;
	}
	
//	public List<AttachmentDto> addAttachments(List<String> filePaths) {
//		List<AttachmentDto> attachments = new ArrayList<AttachmentDto>();
//		filePaths.stream().forEach(path -> {
//			byte[] bArray = Utils.convertFileToByteArray(path);
//			String[] pathPart = path.split("\\.");
//			int lenght = pathPart.length;
//			String MIMEType = Utils.getMIMEType(pathPart[lenght-1]);
//			AttachmentDto attch = new AttachmentDto();
//			attch.setFileName(MIMEType);
//			attch.setBArray(bArray);
//			attachments.add(attch);
//		});
//		return attachments;
//	}
	
	

}
