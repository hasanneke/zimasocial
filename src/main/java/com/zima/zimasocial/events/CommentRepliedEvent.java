package com.zima.zimasocial.events;

import com.zima.zimasocial.entity.CommentEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentRepliedEvent extends ApplicationEvent {
    private final CommentEntity comment;

    public CommentRepliedEvent(Object source, CommentEntity comment) {
        super(source);
        this.comment = comment;
    }
}