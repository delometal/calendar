package com.perigea.tracker.calendar.job;



import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perigea.tracker.calendar.rest.NotificationRestClient;
import com.perigea.tracker.calendar.service.SchedulerService;
import com.perigea.tracker.commons.enums.TipoScheduleEvent;
import com.perigea.tracker.commons.model.Email;

@Component
public class NotificationJob implements Job {

	@Autowired
	private NotificationRestClient notificationRestClient;
	
	@Autowired
	private SchedulerService schedulerService;

	// TODO periodiche con scadenza
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Email email = (Email)context.getJobDetail().getJobDataMap().get("email");
		notificationRestClient.send(email);
		
		Date expiration = (Date)context.getJobDetail().getJobDataMap().get("expiration");
		String tipo = (String)context.getJobDetail().getJobDataMap().get("type");
		if (tipo.equals(Tipo.ISTANTANEA.toString())) {
			schedulerService.disactiveNotification(email.getEventId());
		}else {
			if( expiration != null && new Date().after(expiration) ) {
				schedulerService.disactiveNotification(email.getEventId());
			}
		}
		
		
	}

}
