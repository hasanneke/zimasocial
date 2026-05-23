package com.zima.zimasocial.events;
import com.zima.zimasocial.entity.CommentEntity;
import com.zima.zimasocial.entity.PostJpaEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostCommentedEvent extends ApplicationEvent {
    private final PostJpaEntity post;
    private final CommentEntity comment;

    public PostCommentedEvent(Object source, PostJpaEntity post, CommentEntity comment) {
        super(source);
        this.post = post;
        this.comment = comment;
    }
}
