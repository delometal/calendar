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

import com.perigea.tracker.commons.dto.HolidayEventRequestDto;
import com.perigea.tracker.commons.dto.MeetingEventDto;
import com.perigea.tracker.commons.dto.TimesheetEventDto;
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
	
	@Bean("meetingMessageKafkaConsumerFactory")
	public ConsumerFactory<String, MeetingEventDto> meetingMessageKafkaConsumerFactory() {
		Map<String, Object> consumerProperties = notificationProperties.getKafkaProperties().buildConsumerProperties();
		
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerProperties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MeetingEventDto.class);
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}

	@Bean("meetingMessageKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, MeetingEventDto> meetingMessageKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MeetingEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(meetingMessageKafkaConsumerFactory());
		return factory;
	}
	

	@Bean("timesheetMessageKafkaConsumerFactory")
	public ConsumerFactory<String, TimesheetEventDto> timesheetMessageKafkaConsumerFactory() {
		Map<String, Object> consumerProperties = notificationProperties.getKafkaProperties().buildConsumerProperties();
		
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerProperties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TimesheetEventDto.class);
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}

	@Bean("timesheetMessageKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, TimesheetEventDto> timesheetMessageKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, TimesheetEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(timesheetMessageKafkaConsumerFactory());
		return factory;
	}
	

	@Bean("holidayMessageKafkaConsumerFactory")
	public ConsumerFactory<String, HolidayEventRequestDto> holidayMessageKafkaConsumerFactory() {
		Map<String, Object> consumerProperties = notificationProperties.getKafkaProperties().buildConsumerProperties();
		
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerProperties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, HolidayEventRequestDto.class);
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}

	@Bean("holidayMessageKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, HolidayEventRequestDto> holidayMessageKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, HolidayEventRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(holidayMessageKafkaConsumerFactory());
		return factory;
	}
}
