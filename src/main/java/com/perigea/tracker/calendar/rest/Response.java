package com.perigea.tracker.calendar.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/** wrapper da restituire dai controller contiene status code, timestamp, descrizione insieme a body tipizzabile
 */
@Data
@Builder
public class Response<T> {

	public enum Type {
		SUCCESS,
		ERROR
	}
	
	private Type type;
	private int code;
	private String description;
	@Builder.Default
	private String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
		.format(new Date());
	private T body;
}
