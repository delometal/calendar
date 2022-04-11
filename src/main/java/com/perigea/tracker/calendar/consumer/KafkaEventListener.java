package com.perigea.tracker.calendar.consumer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import com.perigea.tracker.commons.enums.KafkaCalendarEventType;
import com.perigea.tracker.commons.kafkaproperties.TopicProperties;
import com.perigea.tracker.commons.kafkaproperties.TopicProperties.Topic;


public class KafkaEventListener {

	@Autowired
	private TopicProperties topicProperties;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	public List<String> getAllTopicNames(){
		return topicProperties.getTopics().stream().map(Topic::getName).collect(Collectors.toList());
	}
	
	public List<String> getAllTopicConsumerListenerNames(){
		return topicProperties.getTopics().stream().map(Topic::getConsumerListener).collect(Collectors.toList());
	}

	public void startConsuming() {
		getAllTopicConsumerListenerNames().forEach(listenerName -> {
			kafkaListenerEndpointRegistry.getListenerContainer(listenerName).start();
		});
	}
	
	public void stopConsuming() {
		getAllTopicConsumerListenerNames().forEach(listenerName -> {
			kafkaListenerEndpointRegistry.getListenerContainer(listenerName).stop();
		});
	}

	public void startConsuming(KafkaCalendarEventType type) {
		kafkaListenerEndpointRegistry.getListenerContainer(topicProperties.byType(type).getConsumerListener()).start();
	}
	
	public void stopConsuming(KafkaCalendarEventType type) {
		kafkaListenerEndpointRegistry.getListenerContainer(topicProperties.byType(type).getConsumerListener()).stop();
	}
	
}
