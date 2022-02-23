package com.perigea.tracker.calendar.factory;

import com.perigea.tracker.calendar.entity.MeetingEvent;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;


public abstract class ICSFactory {
	
	public static byte[] createICS(MeetingEvent meetingEvent) {
		
		ICalendar ical = new ICalendar();
		VEvent event = new VEvent();
		
		event.setDateStart(meetingEvent.getStartDate());
		event.setDateEnd(meetingEvent.getEndDate());
		event.setDescription(meetingEvent.getDescription());
		event.setOrganizer(meetingEvent.getEventCreator().getMailAziendale());
		event.setUrl(meetingEvent.getLink());
		event.setSummary(meetingEvent.getDescription());
		
		ical.addEvent(event);
		
		return Biweekly.write(ical).go().getBytes();
	}
	
	
}
