package com.perigea.tracker.calendar.consumer.handler;

import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.consumer.ConsumerHandler;
import com.perigea.tracker.commons.dto.TimesheetEventDto;

@Component
public class TimesheetEventMessageHandler implements ConsumerHandler<TimesheetEventDto> {

	@Override
	public void handle(TimesheetEventDto data) {
		
		
	}

}
