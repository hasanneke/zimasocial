package com.zimaberlin.zimasocial.batch.pushnotifications;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class PushNotificationsJob implements Job {
    @Autowired
    private PushNotificationsRunnable pushNotificationsRunnable;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        pushNotificationsRunnable.run();
    }
}
