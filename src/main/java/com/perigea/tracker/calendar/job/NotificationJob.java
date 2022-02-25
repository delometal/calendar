package com.perigea.tracker.calendar.job;



import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.entity.ScheduledEvent.Tipo;
import com.perigea.tracker.calendar.rest.NotificationRestClient;
import com.perigea.tracker.calendar.service.SchedulerService;
import com.perigea.tracker.commons.model.Email;

@Component
public class NotificationJob implements Job {

	@Autowired
	private NotificationRestClient notificationRestClient;
	
	@Autowired
	private SchedulerService schedulerService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Email email = (Email)context.getJobDetail().getJobDataMap().get("email");
		notificationRestClient.send(email);
		
		String tipo = (String)context.getJobDetail().getJobDataMap().get("type");
		if (tipo.equals(Tipo.ISTANTANEA.toString())) {
			schedulerService.disactiveNotification(email.getEventID());
		}
	}

}
