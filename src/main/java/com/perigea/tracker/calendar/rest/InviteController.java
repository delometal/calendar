package com.perigea.tracker.calendar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.tracker.calendar.service.MeetingEventService;
import com.perigea.tracker.commons.dto.ResponseDto;
import com.perigea.tracker.commons.enums.ResponseType;

@RestController
@RequestMapping(path = "/invite")
public class InviteController {
	
	@Autowired
	private MeetingEventService meetingService;
	
	@GetMapping(path = "/accept/{meetingId}/{codicePersona}")
	public ResponseEntity<ResponseDto<String>> acceptInvite(@PathVariable String meetingId, @PathVariable String codicePersona) {
		
		boolean res = meetingService.acceptInvite(meetingId, codicePersona);
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(res ? "Partecipazione confermata" : "Impossibile confermare")
				.type(ResponseType.SUCCESS)
				.code(HttpStatus.OK.value())
				.build(),
				
				HttpStatus.OK);
	}
	
	@GetMapping(path = "/decline/{meetingId}/{codicePersona}")
	public ResponseEntity<ResponseDto<String>> declineInvite(@PathVariable String meetingId, @PathVariable String codicePersona) {
		
		boolean res = meetingService.declineInvite(meetingId, codicePersona);
		return new ResponseEntity<>(
				ResponseDto.<String>builder()
				.data(res ? "Partecipazione annullata" : "Impossibile annullare")
				.type(ResponseType.SUCCESS)
				.code(HttpStatus.OK.value())
				.build(),
				
				HttpStatus.OK);
	}
}
