package com.zima.zimasocial.context.contentmoderation.user;

import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class User {
    private Long authorId;
    private double trustScore;
    private Boolean isBanned;

    public User(Long authorId, Double trustScore, Boolean isBanned) {
        Assert.notNull(authorId, "Author Id cannot be null");
        Assert.notNull(trustScore, "Trust Score cannot be null");
        Assert.notNull(isBanned, "Is Banned cannot be null");
        this.authorId = authorId;
        this.trustScore = trustScore;
        this.isBanned = isBanned;
    }

    public void damageTrustScore() {
        this.trustScore -= 1;
    }

    public void ban() throws TrustScoreNotTooLowYet {
        if(isTrustScoreBelowTheLimit()){
            this.isBanned = true;
        }else{
            throw new TrustScoreNotTooLowYet();
        }
    }

    public void unban() {
        this.isBanned = false;
    }


    private boolean isTrustScoreBelowTheLimit() {
        return trustScore < -9;
    }
}
