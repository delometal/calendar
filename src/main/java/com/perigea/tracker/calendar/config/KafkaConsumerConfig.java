package com.perigea.tracker.calendar.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.kafkaproperties.NotificationProperties;


@Configuration
public class KafkaConsumerConfig {

	private static final String TRUSTED_PACKAGES = "com.perigea.tracker.commons.dto";

	@Autowired
	private NotificationProperties notificationProperties;
	
	@Bean("kafkaConsumerFactory")
	public ConsumerFactory<Object, Object> kafkaConsumerFactory() {
		Map<String, Object> consumerProperties = notificationProperties.getKafkaProperties().buildConsumerProperties();
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}
	@Bean("kafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(kafkaConsumerFactory());
		return factory;
	}
	
	@Bean("auditMessageKafkaConsumerFactory")
	public ConsumerFactory<String, MeetingEventDto> auditMessageKafkaConsumerFactory() {
		Map<String, Object> consumerProperties = notificationProperties.getKafkaProperties().buildConsumerProperties();
		
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerProperties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MeetingEventDto.class);
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}

	@Bean("auditMessageKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, MeetingEventDto> auditMessageKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MeetingEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(auditMessageKafkaConsumerFactory());
		return factory;
	}
}
