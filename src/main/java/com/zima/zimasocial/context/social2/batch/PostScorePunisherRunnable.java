package com.zima.zimasocial.context.social2.batch;

import com.zima.zimasocial.context.social2.application.PostScorePunisherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostScorePunisherRunnable implements Runnable{
    private final PostScorePunisherService postScorePunisherService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public void run() {
        logger.info("--- PostPunisherRunnable started running ---");
        postScorePunisherService.punishPosts();
    }
}
