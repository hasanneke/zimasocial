package com.zima.zimasocial.context.social.infastructure.repository;

import com.zima.zimasocial.context.social.comment.CommentDomain;
import com.zima.zimasocial.context.social.comment.CommentDomainRepository;
import com.zima.zimasocial.context.social.infastructure.adapter.CommentCommentEntityAdapter;
import com.zima.zimasocial.entity.CommentEntity;
import com.zima.zimasocial.entity.PostJpaEntity;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.CommentJpaRepository;
import com.zima.zimasocial.repository.PostJpaRepository;
import com.zima.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zima.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentDBRepository implements CommentDomainRepository {
    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final CommentCommentEntityAdapter commentCommentEntityAdapter;

    @Autowired
    public CommentDBRepository(CommentJpaRepository commentJpaRepository, PostJpaRepository postJpaRepository, CommentCommentEntityAdapter commentCommentEntityAdapter) {
        this.commentJpaRepository = commentJpaRepository;
        this.postJpaRepository = postJpaRepository;
        this.commentCommentEntityAdapter = commentCommentEntityAdapter;
    }

    @Override
    public Optional<CommentDomain> findById(Long id) {
        Optional<CommentEntity> comment = commentJpaRepository.findById(id);
        return Optional.ofNullable(commentCommentEntityAdapter.convertCommentEntityToComment(comment.get()));
    }

    @Override
    public CommentDomain save(CommentDomain comment) {
        CommentEntity commentEntity;
        if(comment.getCommentId() == null){
            commentEntity = new CommentEntity();
            PostJpaEntity post = postJpaRepository.findById(comment.getPostId()).orElseThrow(CommentNotFoundException::new);
            UserEntity user = CurrentUser.getCurrentUserProfile();
            commentEntity.setPost(post);
            commentEntity.setUser(user);
            commentEntity.setContent(comment.getContent());
            commentEntity.setMediaId(comment.getMediaId());
            if(comment.getParentCommentId() != null){
                CommentEntity parent = commentJpaRepository.findById(comment.getParentCommentId()).orElseThrow(CommentNotFoundException::new);
                commentEntity.setParent(parent);
            }
        }else{
            commentEntity = commentJpaRepository.findById(comment.getCommentId()).orElseThrow(CommentNotFoundException::new);
            commentEntity.mergeDomain(comment);
        }
        CommentEntity savedComment = commentJpaRepository.save(commentEntity);
        return commentCommentEntityAdapter.convertCommentEntityToComment(savedComment);
    }

    @Override
    public void saveAll(List<CommentDomain> comments) {
        for (CommentDomain comment : comments) {
            save(comment);
        }
    }

    @Override
    public Page<CommentDomain> findByParentIdOrderByCreatedAt(Long parentId, Pageable pageable) {
        return commentJpaRepository.findByParentIdOrderByCreatedAt(parentId, pageable).map(commentCommentEntityAdapter::convertCommentEntityToComment);
    }

    @Override
    public Page<CommentDomain> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable) {
        return commentJpaRepository.findByPostIdAndParentIdIsNull(postId, pageable).map(commentCommentEntityAdapter::convertCommentEntityToComment);
    }

    @Override
    public void delete(CommentDomain comment) {
        commentJpaRepository.deleteByParentId(comment.getCommentId());
        commentJpaRepository.deleteById(comment.getCommentId());
    }
}
