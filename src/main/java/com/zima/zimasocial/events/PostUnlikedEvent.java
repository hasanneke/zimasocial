package com.zima.zimasocial.events;

import com.zima.zimasocial.entity.PostJpaEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostUnlikedEvent extends ApplicationEvent {
    private final PostJpaEntity post;

    public PostUnlikedEvent(Object source, PostJpaEntity post) {
        super(source);
        this.post = post;
    }
}
