package com.zima.zimasocial.context.social2.application.batch;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class PostScorePunisherJob implements Job {
    @Autowired
    private PostScorePunisherRunnable postPunisherRunnable;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            logger.info("--- PostPunisherJob executed ---");
            postPunisherRunnable.run();
        }catch (Exception e){
            logger.error("--- Encountered exception during PostPunisherBatchJob: %s".formatted(e));
        }
    }
}
