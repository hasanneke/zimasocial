package com.zimaberlin.zimasocial.events;


import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentUnlikedEvent extends ApplicationEvent {
    private final CommentEntity comment;

    public CommentUnlikedEvent(Object source, CommentEntity comment) {
        super(source);
        this.comment = comment;
    }
}