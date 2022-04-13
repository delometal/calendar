package com.perigea.tracker.calendar.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.perigea.tracker.commons.dto.HolidayEventRequestDto;
import com.perigea.tracker.commons.enums.KafkaCalendarEventType;
@Component
public class HolidayEventMessageReceiver extends AbstractKafkaReceiver<HolidayEventRequestDto> {

	@Autowired
	public HolidayEventMessageReceiver(ConsumerHandler<HolidayEventRequestDto> handler) {
		super(handler);
	}
	
	@Override
	public KafkaCalendarEventType eventType() {
		return KafkaCalendarEventType.HOLIDAY_EVENT_MESSAGE;
	}

	@Override
	public Class<HolidayEventRequestDto> entityClass() {

		return HolidayEventRequestDto.class;
	}
	
	@Override
	@KafkaListener(id = "holidayMessageConsumerListener", autoStartup = "true", topics = "notification_service_HOLIDAY_MESSAGE", containerFactory = "holidayMessageKafkaListenerContainerFactory")
	public void onMessage(HolidayEventRequestDto message) {
		System.out.println(message.toString());
		super.onMessage(message);
	}

}
