package com.perigea.tracker.calendar.service;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.configuration.ApplicationProperties;
import com.perigea.tracker.calendar.factory.ICSFactory;
import com.perigea.tracker.commons.dto.AttachmentDto;
import com.perigea.tracker.commons.dto.ContactDto;
import com.perigea.tracker.commons.dto.CreatedUtenteNotificaDto;
import com.perigea.tracker.commons.dto.HolidayEventDto;
import com.perigea.tracker.commons.dto.HolidayEventRequestDto;
import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.dto.TimesheetEventDto;
import com.perigea.tracker.commons.enums.ApprovalStatus;
import com.perigea.tracker.commons.enums.EMese;
import com.perigea.tracker.commons.enums.EmailTemplates;
import com.perigea.tracker.commons.enums.EmailType;
import com.perigea.tracker.commons.exception.HolidayEventException;
import com.perigea.tracker.commons.exception.NullFieldException;
import com.perigea.tracker.commons.exception.URIException;
import com.perigea.tracker.commons.model.Email;
import com.perigea.tracker.commons.utils.NotNullValidator;
import com.perigea.tracker.commons.utils.Utils;

// TODO olverload build() metodi
// TODO accorpare metodi
// TODO possibili metodi statici ??
@Service
public class EventEmailBuilderService {

	@Autowired
	private ApplicationProperties properties;

	public Email build(MeetingEventDto event, String azione, List<File> files) {
		Utils.DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipients = new ArrayList<>();
		Map<String, Object> templateData = new HashMap<>();
		List<AttachmentDto> attachments = new ArrayList<AttachmentDto>();

		if (!NotNullValidator.validate(event))
			throw new NullFieldException(String.format("%s must not be null!", NotNullValidator.getDetails(event)));

		for (ContactDto c : event.getParticipants()) {
			recipients.add(c.getMailAziendale());
		}

		attachments.add(addICSFile(event));

		if (files.size() > 2) {
			AttachmentDto attachment = buildZipAttachment(files);
			attachments.add(attachment);
		} else {
			attachments = addAttachments(files);
		}

		templateData.put("creator",
				String.format("%s %s", event.getEventCreator().getNome(), event.getEventCreator().getCognome()));
		templateData.put("eventType", event.getType());
		templateData.put("dataInizio", Utils.formatDate(event.getStartDate()));
		templateData.put("dataFine", Utils.formatDate(event.getEndDate()));
		templateData.put("partecipanti", recipients);
		templateData.put("azione", azione);
		templateData.put("presenza", event.getInPerson().booleanValue());

		return Email.builder().eventId(event.getId()).from(properties.getEmailSender())
				.templateName(EmailTemplates.MEETING_TEMPLATE.getDescrizione()).templateModel(templateData)
				.subject(String.format("%s - %s", event.getType(), event.getDescription()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipients).attachments(attachments).build();
	}

	public Email buildReminder(MeetingEventDto event) {
		Utils.DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipients = new ArrayList<>();
		for (ContactDto c : event.getParticipants()) {
			recipients.add(c.getMailAziendale());
		}

		List<AttachmentDto> attachments = new ArrayList<AttachmentDto>();
		attachments.add(addICSFile(event));

		Map<String, Object> templateData = new HashMap<>();
		templateData.put("creator",
				String.format("%s %s", event.getEventCreator().getNome(), event.getEventCreator().getCognome()));
		templateData.put("eventType", event.getType());
		templateData.put("dataInizio", Utils.formatDate(event.getStartDate()));
		templateData.put("partecipanti", recipients);

		return Email.builder().eventId(event.getId()).from(properties.getEmailSender())
				.templateName(EmailTemplates.NOTIFICATION_TEMPLATE.getDescrizione()).templateModel(templateData)
				.subject(String.format("REMINDER: %s inizierà a breve", event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipients).attachments(attachments).build();

	}

	public Email build(TimesheetEventDto event, String azione) {
		Utils.DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("ECT"));
		EMese mese = EMese.getByMonthId(event.getTimesheet().getMese());
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getResponsabile().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getEventCreator().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", azione);
		templateData.put("mese", mese.getDescription());
		templateData.put("anno", event.getTimesheet().getAnno());

		return Email.builder().eventId(event.getId()).from(properties.getEmailSender())
				.templateName(EmailTemplates.TIMESHEET_TEMPLATE.getDescrizione()).templateModel(templateData)
				.subject(String.format("%s %s: %s", event.getEventCreator().getNome(),
						event.getEventCreator().getCognome(), event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}

	public Email build(HolidayEventRequestDto event, String azione) {
		Utils.DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getResponsabile().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getEventCreator().getMailAziendale());
		templateData.put("azione", azione);
		templateData.put("events", event.getHolidays());

		return Email.builder().eventId(event.getId()).from(properties.getEmailSender())
				.templateName(EmailTemplates.HOLIDAY_TEMPLATE.getDescrizione()).templateModel(templateData)
				.subject(String.format("%s %s: %s", event.getEventCreator().getNome(),
						event.getEventCreator().getCognome(), event.getType()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}

	public Email buildApproval(TimesheetEventDto event) {
		Utils.DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("ECT"));
		EMese mese = EMese.getByMonthId(event.getTimesheet().getMese());
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getEventCreator().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();
		templateData.put("utente", event.getResponsabile().getMailAziendale());
		templateData.put("eventType", event.getType());
		templateData.put("azione", event.getApprovalStatus().toString().toLowerCase());
		templateData.put("mese", mese.getDescription());
		templateData.put("anno", event.getTimesheet().getAnno());

		return Email.builder().eventId(event.getId()).from(properties.getEmailSender())
				.templateName(EmailTemplates.TIMESHEET_TEMPLATE.getDescrizione()).templateModel(templateData)
				.subject(String.format("%s %s: %s %s", event.getResponsabile().getNome(),
						event.getResponsabile().getCognome(), event.getType(),
						event.getApprovalStatus().toString().toLowerCase()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}

	public Email buildApproval(HolidayEventRequestDto event) {
		Utils.DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("ECT"));
		List<String> recipient = new ArrayList<>();
		recipient.add(event.getEventCreator().getMailAziendale());
		Map<String, Object> templateData = new HashMap<>();

		List<HolidayEventDto> declinedEvents = getDeclinedSingleEvents(event.getHolidays());

		Boolean approved = false;
		if (event.getApproved().equals(ApprovalStatus.APPROVED)) {
			approved = true;
		}

		templateData.put("approved", approved);
		templateData.put("utente", event.getResponsabile().getMailAziendale());
		templateData.put("azione", event.getApproved().toString().toLowerCase());
		templateData.put("events", declinedEvents);

		return Email.builder().eventId(event.getId()).from(properties.getEmailSender())
				.templateName(EmailTemplates.APPROVAL_HOLIDAYS.getDescrizione()).templateModel(templateData)
				.subject(String.format("%s %s: %s %s", event.getResponsabile().getNome(),
						event.getResponsabile().getCognome(), event.getType(),
						event.getApproved().toString().toLowerCase()))
				.emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipient).build();
	}

	public Email build(CreatedUtenteNotificaDto userCredential) {
		Map<String, Object> templateData = new HashMap<>();
		List<String> recipients = new ArrayList<>();
		try {
			recipients.add(userCredential.getMailAziendale());

			templateData.put("utente", userCredential.getNome());
			templateData.put("username", userCredential.getUsername());
			templateData.put("password", userCredential.getPassword());
			templateData.put("token", userCredential.getToken());
			templateData.put("scadenza", userCredential.getDataScadenza());
			templateData.put("link", new URI(properties.getPasswordUpdateEndpoint() + userCredential.getToken()));
		} catch (Exception ex) {
			throw new URIException(ex.getMessage());
		}
		return Email.builder().eventId(UUID.randomUUID().toString()).from(properties.getEmailSender())
				.templateName(EmailTemplates.CREATE_CREDENTIAL_TEMPLATE.getDescrizione()).templateModel(templateData)
				.subject("Attivazione credenziali").emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipients).build();
	}

	public Email buildReminder(CreatedUtenteNotificaDto data) {
		List<String> recipients = new ArrayList<>();
		Map<String, Object> templateData = new HashMap<>();
		try {
			recipients.add(data.getMailAziendale());
			templateData.put("username", data.getUsername());
			templateData.put("link", new URI(properties.getPasswordUpdateEndpoint() + data.getToken()));
		} catch (Exception ex) {
			throw new URIException(ex.getMessage());
		}
		return Email.builder().eventId(UUID.randomUUID().toString()).from(properties.getEmailSender())
				.reminderDate(Utils.shifTimeByHour(data.getDataScadenza(), Utils.CREDENTIAL_REMINDER))
				.templateName(EmailTemplates.REMINDER_CREDENTIAL_TEMPLATE.getDescrizione()).templateModel(templateData)
				.subject("Attivazione credenziali").emailType(EmailType.HTML_TEMPLATE_MAIL).to(recipients).build();
	}

	public AttachmentDto addICSFile(MeetingEventDto event) {
		AttachmentDto attch = new AttachmentDto();
		attch.setBArray(ICSFactory.createICS(event));
		attch.setMIMEType("text/calendar");
		attch.setFilename("meeting.ics");
		return attch;
	}

	public List<HolidayEventDto> getDeclinedSingleEvents(List<HolidayEventDto> list) {
		try {
			List<HolidayEventDto> declinedSingleEvents = new ArrayList<HolidayEventDto>();
			for (HolidayEventDto e : list) {
				if (e.getStatus().equals(ApprovalStatus.DECLINED)) {
					declinedSingleEvents.add(e);
				}
			}
			return declinedSingleEvents;
		} catch (Exception ex) {
			throw new HolidayEventException(ex.getMessage());
		}
	}

	public List<AttachmentDto> addAttachments(List<File> files) {
		List<AttachmentDto> attachments = new ArrayList<AttachmentDto>();
		files.stream().forEach(file -> {
			String path = file.getPath();
			byte[] bArray = Utils.convertFileToByteArray(file);
			String[] pathPart = path.split("\\.");
			String[] pathFinalPart = path.split("\\\\");
			int lenght = pathPart.length;
			String MIMEType = Utils.getMIMEType(pathPart[lenght - 1]);
			String filename = pathFinalPart[pathFinalPart.length - 1];
			AttachmentDto attch = new AttachmentDto();
			attch.setMIMEType(MIMEType);
			attch.setFilename(filename);
			attch.setBArray(bArray);
			attachments.add(attch);
		});
		return attachments;
	}

	public AttachmentDto buildZipAttachment(List<File> files) {

		AttachmentDto attachmentZip = new AttachmentDto();
		byte[] zip = Utils.zipMultipleFiles(files);

		attachmentZip.setMIMEType("application/zip");
		attachmentZip.setBArray(zip);
		attachmentZip.setFilename("Aggregation.zip");

		return attachmentZip;

	}

}
