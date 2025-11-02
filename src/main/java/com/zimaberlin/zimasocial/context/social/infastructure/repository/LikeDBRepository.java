package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.CommentLike;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.LikeAdapter;
import com.zimaberlin.zimasocial.context.social.post.PostLike;
import com.zimaberlin.zimasocial.context.social.like.Like;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.context.social.like.LikeRepository;
import com.zimaberlin.zimasocial.entity.LikeType;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.repository.LikeJpaRepository;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LikeDBRepository implements LikeRepository {
    private final LikeAdapter likeAdapter;
    private final LikeJpaRepository likeJpaRepository;
    private final PostJpaRepository postJpaRepository;
    @Autowired
    public LikeDBRepository(LikeAdapter likeAdapter, LikeJpaRepository likeJpaRepository, PostJpaRepository postJpaRepository) {
        this.likeAdapter = likeAdapter;
        this.likeJpaRepository = likeJpaRepository;
        this.postJpaRepository = postJpaRepository;

    }
    @Override
    public Optional<Like> findByPostIdAndAuthorId(Long postId, AuthorId authorId) {
        Optional<LikeEntity> like = likeJpaRepository.findByUserIdAndPostIdAndType(authorId.getValue(), postId, LikeType.post);
        return like.map(likeAdapter::convertLikeEntityToLikeForPost);
    }

    @Override
    public Optional<CommentLike> findByCommentIdAndAuthorId(Long commentId, AuthorId authorId) {
        return likeJpaRepository.findByUserIdAndCommentId(authorId.getValue(), commentId).map(likeAdapter::convertLikeEntityToLikeForComment);
    }

    @Override
    public void save(Like like) {
        LikeEntity likeEntity = LikeEntity.builder()
                .commentId(like.getCommentId())
                .postId(like.getPostId())
                .userId(like.getAuthorId().getValue())
                .type(like instanceof PostLike ? LikeType.post : LikeType.comment)
                .build();
        likeJpaRepository.save(likeEntity);
    }

    @Override
    public void delete(Like like) {
        LikeEntity likeEntity;
        if(like instanceof PostLike){
            likeEntity =
                    likeJpaRepository.findByUserIdAndPostIdAndType(like.getAuthorId().getValue(), like.getPostId(), LikeType.post).orElseThrow(()-> new ConflictException("Post not liked"));
        }else if(like instanceof CommentLike){
            likeEntity =
                    likeJpaRepository.findByUserIdAndCommentId(like.getAuthorId().getValue(), like.getCommentId()).orElseThrow(()-> new ConflictException("Comment not liked"));
        }else{
            throw new IllegalArgumentException("Unknown type of Like");
        }
        likeEntity.markAsDeleted();
        likeJpaRepository.save(likeEntity);
    }
}
