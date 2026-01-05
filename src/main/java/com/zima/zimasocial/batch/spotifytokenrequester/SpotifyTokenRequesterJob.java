package com.zima.zimasocial.batch.spotifytokenrequester;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class SpotifyTokenRequesterJob implements Job {
    @Autowired
    private SpotifyTokenRequesterRunnable spotifyTokenRequesterRunnable;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        spotifyTokenRequesterRunnable.run();
    }
}
