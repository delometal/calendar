package com.perigea.tracker.calendar.entity;

import com.perigea.tracker.commons.enums.EmployeeType;
import com.perigea.tracker.commons.enums.ParticipationStatus;

import lombok.Data;

@Data
public class EventContact {
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

	private ParticipationStatus participationStatus;
}
