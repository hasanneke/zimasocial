package com.zima.zimasocial.context.social.post.service;

import com.zima.zimasocial.context.social.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PostScoreReducer {
    private static final double hourlyPunishPercentage = 0.04;
    public Integer calculateReduceAmount(Post post) {
        long passedHoursFromCreation = ChronoUnit.HOURS.between(post.getCreatedAt(), OffsetDateTime.now());
        if(passedHoursFromCreation > 72 && post.getScore() < 100) return 0;
        return (int) (post.getScore() * hourlyPunishPercentage);
    }
}
