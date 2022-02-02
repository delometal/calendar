package com.perigea.calendar.exception;
import java.io.Serializable;

public class GenericError implements Serializable{

	private static final long serialVersionUID = 7279651424196414854L;
	
	
	private Integer statusCode;
	private String message;
	
	
	public GenericError() {
	}
	
	
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	

}


