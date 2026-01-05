package com.zima.zimasocial.batch.todaysposts;

import com.zima.zimasocial.calculators.todayspost.TodaysPostGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TodaysPostRunnable implements Runnable{
    private final static Logger logger = LoggerFactory.getLogger(TodaysPostRunnable.class);
    private final TodaysPostGenerator todaysPostGenerator;
    @Override
    public void run() {
        logger.info("Todays Posts Batch Started...");
        try{
            todaysPostGenerator.createTodaysPost();
        }catch (Exception e){
            logger.error("Todays Posts Batch failed");
        }
    }
}



