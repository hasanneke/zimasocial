package com.zima.zimasocial.context.social2.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostStats {
    @Column(name = "like_count")
    private Integer likeCount;
    @Column(name = "comment_count")
    private Integer commentCount;
    @Column(name = "score")
    private Integer score;

    public void incrementLike() {
        likeCount += 1;
    }

    public void incrementComment() {
        commentCount += 1;
    }

    public void decrementComment() {
        commentCount -= 1;
    }
    public void updateScoreBy(Integer value) {
        score += value;
    }

    public void decrementLike() {
        likeCount -= 1;
    }

}
