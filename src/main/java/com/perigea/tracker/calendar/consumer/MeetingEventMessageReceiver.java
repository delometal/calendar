package com.perigea.tracker.calendar.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.enums.KafkaCalendarEventType;

public class MeetingEventMessageReceiver extends AbstractKafkaReceiver<MeetingEventDto> {

	@Autowired
	public MeetingEventMessageReceiver(ConsumerHandler<MeetingEventDto> handler) {
		super(handler);
	}
	
	@Override
	public KafkaCalendarEventType eventType() {
		return KafkaCalendarEventType.RIUNIONE_EVENT_MESSAGE;
	}

	@Override
	public Class<MeetingEventDto> entityClass() {

		return MeetingEventDto.class;
	}
	
	@Override
	@KafkaListener(id = "MeetingEventMessageConsumerListener", autoStartup = "false", topics = "notification_service_ALERT_MESSAGE", containerFactory = "alertMessageKafkaListenerContainerFactory")
	public void onMessage(MeetingEventDto message) {
		super.onMessage(message);
	}

}
