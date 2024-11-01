package com.zimaberlin.zimasocial.events;

import com.zimaberlin.zimasocial.entity.PostEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class PostLikedEvent extends ApplicationEvent {
    private final PostEntity post;

    public PostLikedEvent(Object source, PostEntity post) {
        super(source);
        this.post = post;
    }
}
