package com.perigea.calendar.dto;

import com.perigea.calendar.enums.EmployeeType;
import com.perigea.calendar.enums.ParticipationStatus;

import lombok.Data;

//possono essere mappati a questa classe sia tracker.ContattoDto tracker.UtenteBaseDto
@Data
public class ContactDto {
	
	private Long id;
	private String nome;
	private String cognome;
	private String mailAziendale;
	private String mailPrivata;
	private String cellulare;
	
	//i campi seguenti non fanno parte di ContattoDto del tracker
	private EmployeeType employeeType;
	
	//preferire questo come ID
	private String codicePersona;

	private ParticipationStatus participationStatus = ParticipationStatus.Da_confermare;
	
}
