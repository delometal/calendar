package com.perigea.calendar.entity;

import com.perigea.calendar.enums.EmployeeType;
import com.perigea.calendar.enums.ParticipationStatus;

import lombok.Data;

@Data
public class Contact {
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
