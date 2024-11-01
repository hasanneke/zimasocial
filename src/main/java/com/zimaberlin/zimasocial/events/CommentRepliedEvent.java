package com.zimaberlin.zimasocial.events;

import com.zimaberlin.zimasocial.entity.CommentEntity;
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