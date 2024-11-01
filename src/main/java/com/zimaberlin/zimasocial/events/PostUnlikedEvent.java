package com.zimaberlin.zimasocial.events;

import com.zimaberlin.zimasocial.entity.PostEntity;
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
