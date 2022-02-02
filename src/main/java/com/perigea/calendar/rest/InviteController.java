package com.perigea.calendar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perigea.calendar.service.MeetingEventService;

@RestController
public class InviteController {
	
	@Autowired
	MeetingEventService meetingService;
	
	@GetMapping(path = "/accept_invite")
	public ResponseEntity<Response<String>> acceptInvite(@RequestParam String meetingId, @RequestParam String codicePersona) {
		
		boolean res = meetingService.acceptInvite(meetingId, codicePersona);
		return new ResponseEntity<>(
				Response.<String>builder()
				.body(res ? "Partecipazione confermata" : "Impossibile confermare")
				.type(Response.Type.SUCCESS)
				.code(HttpStatus.OK.value())
				.build(),
				
				HttpStatus.OK);
	}
	
	@GetMapping(path = "/decline_invite")
	public ResponseEntity<Response<String>> declineInvite(@RequestParam String meetingId, @RequestParam String codicePersona) {
		
		boolean res = meetingService.declineInvite(meetingId, codicePersona);
		return new ResponseEntity<>(
				Response.<String>builder()
				.body(res ? "Partecipazione annullata" : "Impossibile annullare")
				.type(Response.Type.SUCCESS)
				.code(HttpStatus.OK.value())
				.build(),
				
				HttpStatus.OK);
	}
}
