package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.calendar.entity.ScheduledEvent.Tipo;
import com.perigea.tracker.calendar.job.NotificationJob;

// TODO: adattare classe agli eventi del calendar

@Service
public class SchedulerService {
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private ScheduledEventRepositoryService repositoryService;

	public ScheduledEvent scheduleEstrazione(Date dataEsecuzione) {	
		JobDetail detail = buildJobDetail();
		Trigger trigger = buildJobTrigger(detail, dataEsecuzione);
		try {
			scheduler.scheduleJob(detail, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ScheduledEvent info = ScheduledEvent.builder()
				.id(detail.getKey().getName())
				.nextFireTime(dataEsecuzione)
				.tipo(Tipo.ISTANTANEA.name())
				.build();
		repositoryService.save(info);
		return info;
	}
	
	public ScheduledEvent scheduleEstrazioneCron(String cron) {
		JobDetail detail = buildJobDetail();
		Trigger trigger = buildCronJobTrigger(detail, cron);
		
		try {
			scheduler.scheduleJob(detail, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ScheduledEvent info = ScheduledEvent.builder()
				.id(detail.getKey().getName())
				.nextFireTime(trigger.getNextFireTime())
				.cron(cron)
				.tipo(Tipo.PERIODICO.name())
				.build();
		repositoryService.save(info);
		return info;
	}

	public boolean deleteEstrazione(String id) throws SchedulerException {
		JobKey key = new JobKey(id, "calendar");
		repositoryService.deleteJobById(id);
		return scheduler.deleteJob(key);
	}
	
	public ScheduledEvent reschedule(Date nuovaData, String id) throws SchedulerException {
		JobKey jobKey = new JobKey(id, "calendar");
		if (!scheduler.checkExists(jobKey)) {
			return null;
		}
		TriggerKey triggerKey = new TriggerKey(id, "calendar");
		JobDetail detail = buildJobDetail();
		Trigger trigger = buildJobTrigger(detail, nuovaData, id);
		if (scheduler.rescheduleJob(triggerKey, trigger) == null) {
			return new ScheduledEvent();
		}

		ScheduledEvent info = ScheduledEvent.builder()
				.id(detail.getKey().getName())
				.nextFireTime(nuovaData)
				.tipo(Tipo.ISTANTANEA.name())
				.build();
		repositoryService.save(info);
		return info;
	}

	public ScheduledEvent reschedule(String cron, String id) throws SchedulerException {
		JobKey jobKey = new JobKey(id, "calendar");
		if (!scheduler.checkExists(jobKey)) {
			return null;
		}
		TriggerKey triggerKey = new TriggerKey(id, "calendar");
		JobDetail detail = scheduler.getJobDetail(jobKey);
		Trigger trigger = buildCronJobTrigger(detail, cron, id);
		if (scheduler.rescheduleJob(triggerKey, trigger) == null) {
			return new ScheduledEvent();
		}

		ScheduledEvent info = ScheduledEvent.builder()
				.id(detail.getKey().getName())
				.cron(cron)
				.tipo(Tipo.PERIODICO.name())
				.build();
		repositoryService.save(info);
		return info;
	}

	public void schedule(ScheduledEvent info) throws SchedulerException {
		if (info.getTipo().equals(Tipo.PERIODICO.name())) {
			reschedule(info.getCron(), info.getId());
		} else {
			reschedule(info.getNextFireTime(), info.getId());
		}
	}
	
	public void rescheduleAvvio() { 
		List<ScheduledEvent> infos = repositoryService.getAll();
		
		repositoryService.deleteAll();
		
		try {
			for (ScheduledEvent scheduledInfo : infos) {
				JobDetail detail = buildJobDetail();
				Trigger trigger;
				
				if (scheduledInfo.getTipo().equals(Tipo.PERIODICO.name())) {
					trigger = buildCronJobTrigger(detail, scheduledInfo.getCron());
				} else {
					trigger = buildJobTrigger(detail, scheduledInfo.getNextFireTime());
				}
				
				scheduler.scheduleJob(detail, trigger);
				
				scheduledInfo.setId(detail.getKey().getName());
				repositoryService.save(scheduledInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<ScheduledEvent> getAll() {
		return repositoryService.getAll();
	}
	
    private JobDetail buildJobDetail() {
		return JobBuilder.newJob(NotificationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "calendar")
                .withDescription("Job scheduler for notification")
                .build();
    }
    
    private Trigger buildJobTrigger(JobDetail detail, Date dataEsecuzione) {
    	return buildJobTrigger(detail, dataEsecuzione, detail.getKey().getName());
    }
    
    private Trigger buildJobTrigger(JobDetail detail, Date dataEsecuzione, String id) {
    	return TriggerBuilder.newTrigger()
				.forJob(detail)
				.withIdentity(id, detail.getKey().getGroup())
				.withDescription(detail.getDescription())
				.startAt(Date.from(dataEsecuzione.toInstant()))
				.build();
	}
    
    private CronTrigger buildCronJobTrigger(JobDetail detail, String cron) {
    	return buildCronJobTrigger(detail, cron, detail.getKey().getName());
	}
    
    private CronTrigger buildCronJobTrigger(JobDetail detail, String cron, String id) {
    	return TriggerBuilder.newTrigger()
				.forJob(detail)
				.withIdentity(id, detail.getKey().getGroup())
				.withDescription(detail.getDescription())
				.withSchedule(CronScheduleBuilder.cronSchedule(cron))
				.build();
	}
}