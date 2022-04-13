package com.perigea.tracker.calendar.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.perigea.tracker.commons.dto.TimesheetEventDto;
import com.perigea.tracker.commons.enums.KafkaCalendarEventType;
@Component
public class TimesheetEventMessageReceiver extends AbstractKafkaReceiver<TimesheetEventDto> {

	@Autowired
	public TimesheetEventMessageReceiver(ConsumerHandler<TimesheetEventDto> handler) {
		super(handler);
	}
	
	@Override
	public KafkaCalendarEventType eventType() {
		return KafkaCalendarEventType.TIMESHEET_EVENT_MESSAGE;
	}

	@Override
	public Class<TimesheetEventDto> entityClass() {

		return TimesheetEventDto.class;
	}
	
	@Override
	@KafkaListener(id = "timesheetMessageConsumerListener", autoStartup = "true", topics = "notification_service_TIMESHEET_MESSAGE", containerFactory = "timesheetMessageKafkaListenerContainerFactory")
	public void onMessage(TimesheetEventDto message) {
		System.out.println(message.toString());
		super.onMessage(message);
	}

}
