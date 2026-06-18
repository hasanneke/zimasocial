package com.zima.zimasocial.config.quartz;

import com.zima.zimasocial.context.social.media.batch.SpotifyTokenRequesterJob;
import com.zima.zimasocial.context.social.post.batch.TodaysPostJob;
import com.zima.zimasocial.context.social.post.batch.PostScorePunisherJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
@Profile({"dev"})
public class QuartzConfigDev {
    @Bean(name = "todaysPostJobDetail")
    public JobDetail todaysPostJobDetail() {
        return JobBuilder.newJob().ofType(TodaysPostJob.class)
                .storeDurably()
                .withIdentity("Qrtz_Todays_Posts_Job_Detail")
                .withDescription("Invoke TodaysPostsJob")
                .build();
    }
    @Bean(name = "todaysPostTrigger")
    public Trigger todaysPostTrigger(@Qualifier("todaysPostJobDetail") JobDetail jobDetail){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("Qrtz_Todays_Post_Trigger")
                .withDescription("Quartz Todays Posts Trigger")
                .withSchedule(cronSchedule("0 5 0 * * ?")
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }
//    @Bean(name = "pushNotificationsJobDetail")
//    public JobDetail pushNotificationsJobDetail() {
//        return JobBuilder.newJob().ofType(PushNotificationsJob.class)
//                .storeDurably()
//                .withIdentity("Qrtz_Push_Notifications_JobDetail")
//                .withDescription("Invoke TodaysPostsJob")
//                .build();
//    }
//    @Bean(name = "pushNotificationsTrigger")
//    public Trigger pushNotificationsTrigger(@Qualifier("pushNotificationsJobDetail") JobDetail jobDetail){
//        return TriggerBuilder.newTrigger()
//                .forJob(jobDetail)
//                .withIdentity("Qrtz_Push_Notifications_Trigger")
//                .withDescription("Quartz Triggers push notifications for mobile clients")
//                .withSchedule(cronSchedule("0/2 * * * * ?")
//                        .withMisfireHandlingInstructionFireAndProceed())
//                .build();
//    }

    @Bean(name = "spotifyTokenRefresherJobDetail")
    public JobDetail spotifyTokenRefresherJobDetail() {
        return JobBuilder.newJob().ofType(SpotifyTokenRequesterJob.class)
                .storeDurably()
                .withIdentity("Qrtz_Spotify_Token_Refresh_JobDetail")
                .withDescription("Invoke SpotiTokenRefresher")
                .build();
    }
    @Bean(name = "spotifyTokenRefresherTrigger")
    public Trigger spotifyTokenRefresherTrigger(@Qualifier("spotifyTokenRefresherJobDetail") JobDetail jobDetail){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("Qrtz_Spoti_Token_Refresher_Trigger")
                .withDescription("Quartz refreshes spotify access token")
                .startNow()
                .withSchedule(cronSchedule("0 0/30 * * * ?").withMisfireHandlingInstructionFireAndProceed())
                .build();
    }
    @Bean(name = "postScorePunisherJobDetail")
    public JobDetail postScorePunisherJobDetail() {
        return JobBuilder.newJob().ofType(PostScorePunisherJob.class)
                .storeDurably()
                .withIdentity("Qrtz_Post_Punisher_Job")
                .withDescription("Batch to reduce post scores")
                .requestRecovery()
                .build();
    }
    @Bean(name = "postScorePunisherTrigger")
    public Trigger postScorePunisherTrigger(@Qualifier("postScorePunisherJobDetail") JobDetail jobDetail){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("Qrtz_Post_Score_Punisher_Trigger")
                .withDescription("Batch reduces score of posts every hour wit given parameters")
                .withSchedule(cronSchedule("0 0 * * * ?")
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }
}
