package com.zima.zimasocial.config;

import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuartzStartupTrigger {

    private final Scheduler scheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void triggerOnStartup() throws SchedulerException {
        if(!scheduler.checkExists(JobKey.jobKey("Qrtz_Spotify_Token_Refresh_JobDetail"))) return;
        scheduler.triggerJob(JobKey.jobKey("Qrtz_Spotify_Token_Refresh_JobDetail"));
        scheduler.triggerJob(JobKey.jobKey("Qrtz_Post_Punisher_Job"));
    }
}