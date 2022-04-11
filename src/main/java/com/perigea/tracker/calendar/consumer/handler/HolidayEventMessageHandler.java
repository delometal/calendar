package com.perigea.tracker.calendar.consumer.handler;

import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.consumer.ConsumerHandler;
import com.perigea.tracker.commons.dto.HolidayEventDto;

@Component
public class HolidayEventMessageHandler implements ConsumerHandler<HolidayEventDto> {

	@Override
	public void handle(HolidayEventDto data) {
		
		
	}

}
