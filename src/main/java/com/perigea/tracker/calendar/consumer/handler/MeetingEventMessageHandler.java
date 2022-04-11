package com.perigea.tracker.calendar.consumer.handler;

import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.consumer.ConsumerHandler;
import com.perigea.tracker.commons.dto.MeetingEventDto;

@Component
public class MeetingEventMessageHandler implements ConsumerHandler<MeetingEventDto> {

	@Override
	public void handle(MeetingEventDto data) {
		
		
	}

}
