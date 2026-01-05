package com.zima.zimasocial.events;
import com.zima.zimasocial.entity.CommentEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentDeletedEvent extends ApplicationEvent {
    private final CommentEntity comment;

    public CommentDeletedEvent(Object source, CommentEntity comment) {
        super(source);
        this.comment = comment;
    }
}