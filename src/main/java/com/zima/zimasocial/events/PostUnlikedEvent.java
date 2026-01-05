package com.zima.zimasocial.events;

import com.zima.zimasocial.entity.PostEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostUnlikedEvent extends ApplicationEvent {
    private final PostEntity post;

    public PostUnlikedEvent(Object source, PostEntity post) {
        super(source);
        this.post = post;
    }
}
