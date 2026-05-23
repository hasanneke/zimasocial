package com.zima.zimasocial.events;

import com.zima.zimasocial.entity.PostJpaEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostLikedEvent extends ApplicationEvent {
    private final PostJpaEntity post;

    public PostLikedEvent(Object source, PostJpaEntity post) {
        super(source);
        this.post = post;
    }
}
