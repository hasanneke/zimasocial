package com.zimaberlin.zimasocial.calculators;

import com.zimaberlin.zimasocial.entity.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostScoreCalculator {
    final static double likeScoreRatio = 1.5;
    final static double commentScoreRatio = 3.5;
    public double calculateScore(PostEntity post) {
        int likeCount = post.getLikeCount();
        int commentCount = post.getCommentCount();
        return ((likeCount * likeScoreRatio) + (commentCount * commentScoreRatio));
    }
}