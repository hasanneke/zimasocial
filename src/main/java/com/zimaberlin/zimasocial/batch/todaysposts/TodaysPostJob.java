package com.zimaberlin.zimasocial.batch.todaysposts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TodaysPostJob implements Job {
    private TodaysPostRunnable todaysPostRunnable;
    @Override
    public void execute(JobExecutionContext context) {
        todaysPostRunnable.run();
    }
}
