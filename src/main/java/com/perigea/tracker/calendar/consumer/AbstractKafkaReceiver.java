package com.perigea.tracker.calendar.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.perigea.tracker.commons.enums.KafkaCalendarEventType;



public abstract class AbstractKafkaReceiver<T> {

protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	protected ConsumerHandler<T> handler;
	
	public abstract KafkaCalendarEventType eventType();

	public abstract Class<T> entityClass();
	
	public AbstractKafkaReceiver(ConsumerHandler<T> handler) {
		this.handler = handler;
	}
	
	public void onMessage(T data) {
		if(logger.isDebugEnabled()) {
			logger.debug("Received message of type {}, data {}: ",  eventType(), data);			
		}
		this.handler.handle(data);
	}
	
}
