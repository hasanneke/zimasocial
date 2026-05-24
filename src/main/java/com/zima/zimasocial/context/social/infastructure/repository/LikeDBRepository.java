package com.zima.zimasocial.context.social.infastructure.repository;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.comment.CommentLike;
import com.zima.zimasocial.context.social.infastructure.adapter.LikeAdapter;
import com.zima.zimasocial.context.social.like.LikeDomain;
import com.zima.zimasocial.context.social.like.LikeDomainRepository;
import com.zima.zimasocial.context.social.post.value.PostLike;
import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.repository.LikeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LikeDBRepository implements LikeDomainRepository {
    private final LikeAdapter likeAdapter;
    private final LikeJpaRepository likeJpaRepository;

    @Autowired
    public LikeDBRepository(LikeAdapter likeAdapter, LikeJpaRepository likeJpaRepository) {
        this.likeAdapter = likeAdapter;
        this.likeJpaRepository = likeJpaRepository;
    }
    @Override
    public Optional<LikeDomain> findByPostIdAndAuthorId(Long postId, AuthorDomainId authorId) {
        Optional<LikeEntity> like = likeJpaRepository.findByUserIdAndPostIdAndType(authorId.getValue(), postId, LikeType.post);
        return like.map(likeAdapter::convertLikeEntityToLikeForPost);
    }

    @Override
    public Optional<CommentLike> findByCommentIdAndAuthorId(Long commentId, AuthorDomainId authorId) {
        return likeJpaRepository.findByUserIdAndCommentId(authorId.getValue(), commentId).map(likeAdapter::convertLikeEntityToLikeForComment);
    }

    @Override
    public void save(LikeDomain like) {
        LikeEntity likeEntity = LikeEntity.builder()
                .commentId(like.getCommentId())
                .postId(like.getPostId())
                .userId(like.getAuthorId().getValue())
                .type(like instanceof PostLike ? LikeType.post : LikeType.comment)
                .build();
        likeJpaRepository.save(likeEntity);
    }

    @Override
    public void delete(LikeDomain like) {
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
        likeJpaRepository.delete(likeEntity);
    }
}
