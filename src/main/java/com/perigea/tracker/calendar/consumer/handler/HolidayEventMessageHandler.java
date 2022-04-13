package com.perigea.tracker.calendar.consumer.handler;

import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.consumer.ConsumerHandler;
import com.perigea.tracker.commons.dto.HolidayEventRequestDto;

@Component
public class HolidayEventMessageHandler implements ConsumerHandler<HolidayEventRequestDto> {

	@Override
	public void handle(HolidayEventRequestDto data) {
		
		
	}

}
