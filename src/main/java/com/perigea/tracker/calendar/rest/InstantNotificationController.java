package com.perigea.tracker.calendar.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.service.EventEmailBuilderService;
import com.perigea.tracker.calendar.service.SchedulerService;
import com.perigea.tracker.commons.dto.CreatedUtenteNotificaDto;
import com.perigea.tracker.commons.dto.NonPersistedEventDto;
import com.perigea.tracker.commons.exception.NullFieldException;
import com.perigea.tracker.commons.model.Email;
import com.perigea.tracker.commons.utils.NotNullValidator;
import com.perigea.tracker.commons.utils.Utils;

@RestController
@RequestMapping(path = "/instant-notification")
public class InstantNotificationController {

	@Autowired
	private EventEmailBuilderService emailBuilder;
	
	@Autowired
	private NotificationRestClient notificator;
	
	@Autowired
	private SchedulerService scheduler;
	
	@PostMapping(path = "/user-created")
	public ResponseEntity<String> notificaCreazioneUtente(@RequestBody NonPersistedEventDto<CreatedUtenteNotificaDto> notifica){
		String data = notifica.getData();
		CreatedUtenteNotificaDto userCredential = Utils.toObject(data, notifica.getClazz());	
		if (!NotNullValidator.validate(userCredential)) {
			throw new NullFieldException(String.format("%s must not be null!", NotNullValidator.getDetails(userCredential)));
		}
		Email message = emailBuilder.build(userCredential);
		notificator.send(message);
		Email reminder = emailBuilder.buildReminder(userCredential);
		Date nextFire = Utils.shifTimeByHour(userCredential.getDataScadenza(), Utils.CREDENTIAL_EXPIRATION_SHIFT_AMOUNT);
		scheduler.scheduleNotifica(nextFire, reminder);
		return ResponseEntity.ok("OK");
	}
	
}
