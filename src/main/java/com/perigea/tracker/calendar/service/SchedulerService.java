package com.perigea.tracker.calendar.service;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.calendar.job.NotificationJob;
import com.perigea.tracker.commons.enums.EventStatus;
import com.perigea.tracker.commons.enums.TipoScheduleEvent;
import com.perigea.tracker.commons.exception.NotificationSchedulerException;
import com.perigea.tracker.commons.model.Email;

@Service
public class SchedulerService {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	Logger logger;

	@Autowired
	private ScheduledEventRepositoryService repositoryService;

	public ScheduledEvent scheduleNotifica(Date dataEsecuzione, Email email) {
		JobDetail detail = buildJobDetail(email, TipoScheduleEvent.ISTANTANEA.toString());
		Trigger trigger = buildJobTrigger(detail, dataEsecuzione);
		try {
			Date nextFire = scheduler.scheduleJob(detail, trigger);
			logger.info(String.format("Notifica schedulata in data: %s", nextFire));
		} catch (Exception e) {
			throw new NotificationSchedulerException(e.getMessage());
		}

		ScheduledEvent info = ScheduledEvent.builder().email(email).id(detail.getKey().getName())
				.nextFireTime(dataEsecuzione).tipo(TipoScheduleEvent.ISTANTANEA.name()).status(EventStatus.Active)
				.build();
		repositoryService.save(info);
		return info;
	}

	public ScheduledEvent scheduleNotificaPeriodica(String cron, Email email, Date expiration) {
		JobDetail detail = buildJobDetailPeriodic(email, TipoScheduleEvent.PERIODICO.toString(), expiration);
		Trigger trigger = buildCronJobTrigger(detail, cron);
		try {
			Date nextFire = scheduler.scheduleJob(detail, trigger);
			logger.info(String.format("Notifica periodica in data: ", nextFire));
		} catch (Exception e) {
			throw new NotificationSchedulerException(e.getMessage());
		}

		ScheduledEvent info = ScheduledEvent.builder().email(email).id(detail.getKey().getName())
				.nextFireTime(trigger.getNextFireTime()).cron(cron).tipo(TipoScheduleEvent.PERIODICO.name())
				.expiration(expiration).status(EventStatus.Active).build();
		repositoryService.save(info);
		return info;
	}

	public String pauseNotification(String id) {
		try {
			JobKey key = new JobKey(id, "calendar");
			ScheduledEvent event = repositoryService.getById(id);
			scheduler.pauseJob(key);
			event.setStatus(EventStatus.Paused);
			repositoryService.save(event);
			return "ScheduleEvent paused";
		} catch (Exception e) {
			throw new NotificationSchedulerException(e.getMessage());
		}

	}

	public ScheduledEvent resumeNotification(String id) {
		try {
			ScheduledEvent event = repositoryService.getByStatusAndId(EventStatus.Paused, id);
			JobKey jobKey = new JobKey(id, "calendar");
			if (!scheduler.checkExists(jobKey)) {
				return null;
			}
			scheduler.resumeJob(jobKey);
			event.setStatus(EventStatus.Active);
			repositoryService.save(event);
			return event;
		} catch (Exception e) {
			throw new NotificationSchedulerException(e.getMessage());
		}
	}

	public boolean disactiveNotification(String id) {
		try {
			JobKey key = new JobKey(id, "calendar");
//			repositoryService.deleteJobById(id);
			ScheduledEvent event = repositoryService.getById(id);
			event.setStatus(EventStatus.Inactive);
			repositoryService.save(event);
			return scheduler.deleteJob(key);
		} catch (Exception e) {
			throw new NotificationSchedulerException(e.getMessage());
		}
	}

	public ScheduledEvent reschedule(Date nuovaData, String id, Email email) {

		JobDetail detail = buildJobDetail(email, repositoryService.getById(id).getTipo());
		JobKey jobKey = new JobKey(id, "calendar");
		try {
			if (!scheduler.checkExists(jobKey)) {
				return null;
			}
			TriggerKey triggerKey = new TriggerKey(id, "calendar");
			Trigger trigger = buildJobTrigger(detail, nuovaData, id);
			scheduler.rescheduleJob(triggerKey, trigger);
			logger.info(String.format("Notifica rischedulata in data %s", nuovaData));
			ScheduledEvent info = ScheduledEvent.builder().email(email).id(detail.getKey().getName())
					.nextFireTime(nuovaData).tipo(TipoScheduleEvent.ISTANTANEA.name()).status(EventStatus.Active)
					.build();
			repositoryService.save(info);
			return info;
		} catch (Exception e) {
			throw new NotificationSchedulerException(e.getMessage());
		}
	}

	public ScheduledEvent reschedule(String cron, String id) {
		JobKey jobKey = new JobKey(id, "calendar");
		try {
			if (!scheduler.checkExists(jobKey)) {
				return null;
			}
			TriggerKey triggerKey = new TriggerKey(id, "calendar");
			JobDetail detail = scheduler.getJobDetail(jobKey);
			Trigger trigger = buildCronJobTrigger(detail, cron, id);
			if (scheduler.rescheduleJob(triggerKey, trigger) == null) {
				return new ScheduledEvent();
			}

			ScheduledEvent info = ScheduledEvent.builder().email((Email) detail.getJobDataMap().get("email"))
					.id(detail.getKey().getName()).cron(cron).tipo(TipoScheduleEvent.PERIODICO.name())
					.status(EventStatus.Active).build();
			repositoryService.save(info);
			return info;
		} catch (Exception e) {
			throw new NotificationSchedulerException(e.getMessage());
		}
	}

	public void rescheduleAvvio() {
		List<ScheduledEvent> infos = repositoryService.getAllByStatus(EventStatus.Active);
		repositoryService.deleteAll(infos);
		try {
			for (ScheduledEvent scheduledInfo : infos) {
				JobDetail detail = buildJobDetail(scheduledInfo.getEmail(), scheduledInfo.getTipo());
				Trigger trigger;

				if (scheduledInfo.getTipo().equals(TipoScheduleEvent.PERIODICO.name())) {
					trigger = buildCronJobTrigger(detail, scheduledInfo.getCron());
				} else {
					trigger = buildJobTrigger(detail, scheduledInfo.getNextFireTime());
				}
				logger.info("JOB rischedulato");
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

	private JobDetail buildJobDetail(Email email, String type) {
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("email", email);
		dataMap.put("type", type);
		return JobBuilder.newJob(NotificationJob.class).withIdentity(email.getEventId(), "calendar")
				.usingJobData(dataMap).withDescription("Job scheduler for notification").build();
	}

	private JobDetail buildJobDetailPeriodic(Email email, String type, Date expiration) {
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("email", email);
		dataMap.put("type", type);
		dataMap.put("expiration", expiration);
		return JobBuilder.newJob(NotificationJob.class).withIdentity(email.getEventId(), "calendar")
				.usingJobData(dataMap).withDescription("Job scheduler for notification").build();
	}

	private Trigger buildJobTrigger(JobDetail detail, Date dataEsecuzione) {
		return buildJobTrigger(detail, dataEsecuzione, detail.getKey().getName());
	}

	private Trigger buildJobTrigger(JobDetail detail, Date dataEsecuzione, String id) {
		return TriggerBuilder.newTrigger().forJob(detail).withIdentity(id, detail.getKey().getGroup())
				.withDescription(detail.getDescription()).startAt(Date.from(dataEsecuzione.toInstant())).build();
	}

	private CronTrigger buildCronJobTrigger(JobDetail detail, String cron) {
		return buildCronJobTrigger(detail, cron, detail.getKey().getName());
	}

	private CronTrigger buildCronJobTrigger(JobDetail detail, String cron, String id) {
		return TriggerBuilder.newTrigger().forJob(detail).withIdentity(id, detail.getKey().getGroup())
				.withDescription(detail.getDescription()).withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
	}
}