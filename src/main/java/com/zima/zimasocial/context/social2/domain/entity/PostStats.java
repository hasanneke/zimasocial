package com.zima.zimasocial.context.social2.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStats {
    @Column(name = "like_count")
    private Integer likeCount = 0;
    @Column(name = "comment_count")
    private Integer commentCount = 0;
    @Column(name = "score")
    private Integer score = 100;

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
