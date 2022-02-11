package com.perigea.tracker.calendar.job;



import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.rest.NotificationRestClient;
import com.perigea.tracker.commons.model.Email;

@Component
public class NotificationJob implements Job {

	@Autowired
	private NotificationRestClient notificationRestClient;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		notificationRestClient.mandaNotifica((Email)context.getJobDetail().getJobDataMap().get("email"));
	}

}
