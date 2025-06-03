package com.zimaberlin.zimasocial.config;

import com.zimaberlin.zimasocial.batch.todaysposts.TodaysPostJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
public class QuartzConfig {
    @Bean
    public Scheduler scheduler(Trigger trigger, JobDetail job, SchedulerFactoryBean factory)
            throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        scheduler.deleteJob(JobKey.jobKey("Qrtz_Todays_Posts_Job_Detail"));
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
        return scheduler;
    }
    @Bean
    public JobDetail todaysPostJobDetail() {
        return JobBuilder.newJob().ofType(TodaysPostJob.class)
                .storeDurably()
                .withIdentity("Qrtz_Todays_Posts_Job_Detail")
                .withDescription("Invoke TodaysPostsJob")
                .build();
    }
    @Bean
    public Trigger trigger(JobDetail jobDetail){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("Qrtz_Todays_Post_Trigger")
                .withDescription("Quartz Todays Posts Trigger")
                .withSchedule(cronSchedule("0 5 0 * * ?")
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }
}
