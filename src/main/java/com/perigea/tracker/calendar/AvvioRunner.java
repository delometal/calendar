package com.perigea.tracker.calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.service.SchedulerService;


@Component
public class AvvioRunner implements ApplicationRunner {
	
	@Autowired
	private SchedulerService schedulerService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		schedulerService.rescheduleAvvio();
	}

}
