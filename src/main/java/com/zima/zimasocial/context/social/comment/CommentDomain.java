package com.zima.zimasocial.context.social.comment;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CommentDomain {
    private Long commentId;
    private Long parentCommentId;
    private Long postId;
    private AuthorDomainId authorId;
    private String content;
    private int likeCount;
    private int replyCount;
    private UUID mediaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDomain(Long commentId, Long parentCommentId, Long postId, UUID mediaId, AuthorDomainId authorId, String content, int likeCount, int replyCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Assert.notNull(commentId, "Comment Id cannot be null");
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.notNull(authorId, "Author Id cannot be null");
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.mediaId = mediaId;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }
    public CommentDomain(Long postId, Long parentCommentId, AuthorDomainId authorId, String content, UUID mediaId) {
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.notNull(authorId, "Author Id cannot be null");
        this.parentCommentId = parentCommentId;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.likeCount = 0;
        this.replyCount = 0;
        this.mediaId = mediaId;
    }
    public CommentDomain reply(AuthorDomainId replierAuthorId, String comment, UUID mediaId){
        replyCount += 1;
        return new CommentDomain(postId, commentId, replierAuthorId, comment, mediaId);
    }
    public void removeReply(CommentDomain reply) {
        replyCount -= 1;
    }

    public CommentLike like(AuthorDomain liker){
        likeCount += 1;
        StaticEventPublisher.publishEvent(new CommentLikedEvent(this.postId, this.getCommentId(), authorId, liker.getId()));
        return new CommentLike(postId, liker.getId(), commentId);
    }

    public void unlike(){
        likeCount -= 1;
    }
}
