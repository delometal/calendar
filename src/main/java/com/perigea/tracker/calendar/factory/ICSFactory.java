package com.perigea.tracker.calendar.factory;

import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.utils.Utils;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;


public abstract class ICSFactory {
	
	public static byte[] createICS(MeetingEventDto meetingEvent) {
		
		ICalendar ical = new ICalendar();
		VEvent event = new VEvent();
		
		event.setDateStart(Utils.convertToDateViaInstant(meetingEvent.getStartDate()));
		event.setDateEnd(Utils.convertToDateViaInstant(meetingEvent.getEndDate()));
		event.setDescription(meetingEvent.getDescription());
		event.setOrganizer(meetingEvent.getEventCreator().getMailAziendale());
		event.setUrl(meetingEvent.getLink());
		event.setSummary(meetingEvent.getDescription());
		
		ical.addEvent(event);
		
		return Biweekly.write(ical).go().getBytes();
	}
	
	
}
