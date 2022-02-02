package com.perigea.calendar.factory;

import com.perigea.calendar.dto.MeetingEventDto;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;


public abstract class ICSFactory {
	
	public static byte[] createICS(MeetingEventDto meeting) {
		
		ICalendar ical = new ICalendar();
		VEvent event = new VEvent();
		
		event.setDateStart(meeting.getStartDate());
		event.setDateEnd(meeting.getEndDate());
		event.setDescription(meeting.getDescritpion());
		event.setOrganizer(meeting.getEventCreator().getMailAziendale());
		event.setUrl(meeting.getLink());
		
		ical.addEvent(event);
		return Biweekly.write(ical).go().getBytes();
	}
}
