package com.perigea.tracker.calendar.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.enums.KafkaCalendarEventType;
@Component
public class MeetingEventMessageReceiver extends AbstractKafkaReceiver<MeetingEventDto> {

	@Autowired
	public MeetingEventMessageReceiver(ConsumerHandler<MeetingEventDto> handler) {
		super(handler);
	}
	
	@Override
	public KafkaCalendarEventType eventType() {
		return KafkaCalendarEventType.MEETING_EVENT_MESSAGE;
	}

	@Override
	public Class<MeetingEventDto> entityClass() {

		return MeetingEventDto.class;
	}
	
	@Override
	@KafkaListener(id = "meetingMessageConsumerListener", autoStartup = "true", topics = "notification_service_RIUNIONE_MESSAGE", containerFactory = "meetingMessageKafkaListenerContainerFactory")
	public void onMessage(MeetingEventDto message) {
		System.out.println(message.toString());
		super.onMessage(message);
	}

}
