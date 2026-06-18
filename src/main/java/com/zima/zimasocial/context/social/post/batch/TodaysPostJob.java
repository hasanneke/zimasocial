package com.zima.zimasocial.context.social.post.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TodaysPostJob implements Job {
    @Autowired
    private TodaysPostRunnable todaysPostRunnable;

    @Override
    public void execute(JobExecutionContext context) {
        todaysPostRunnable.run();
    }
}
