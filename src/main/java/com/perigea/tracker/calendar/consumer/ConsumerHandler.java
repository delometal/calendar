package com.perigea.tracker.calendar.consumer;

public interface ConsumerHandler<T> {
	public void handle(T data);
}
