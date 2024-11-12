package com.zimaberlin.zimasocial.events;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostCommentedEvent extends ApplicationEvent {
    private final PostEntity post;
    private final CommentEntity comment;

    public PostCommentedEvent(Object source, PostEntity post, CommentEntity comment) {
        super(source);
        this.post = post;
        this.comment = comment;
    }
}
