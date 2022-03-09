package com.perigea.tracker.calendar.model;

import java.io.Serializable;

import com.perigea.tracker.commons.enums.AnagraficaType;
import com.perigea.tracker.commons.enums.ParticipationStatus;

import lombok.Data;

@Data
public class Contact implements Serializable {
	
	private static final long serialVersionUID = 1380106797597898605L;
	
	private String codicePersona;
	private String username;
	private String nome;
	private String cognome;
	private String mailAziendale;
	private String mailPrivata;
	private String cellulare;
	private AnagraficaType tipo;
	private ParticipationStatus participationStatus;
	
}
