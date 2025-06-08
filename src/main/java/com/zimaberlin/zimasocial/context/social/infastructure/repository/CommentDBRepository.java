package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.CommentCommentEntityAdapter;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.CommentJpaRepository;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CommentDBRepository implements CommentRepository {
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
    public Optional<Comment> findById(Long id) {
        Optional<CommentEntity> comment = commentJpaRepository.findById(id);
        return Optional.ofNullable(commentCommentEntityAdapter.convertCommentEntityToComment(comment.get()));
    }

    @Override
    public Comment save(Comment comment) {
        CommentEntity commentEntity;
        if(comment.getCommentId() == null){
            commentEntity = new CommentEntity();
            PostEntity post = postJpaRepository.findById(comment.getPostId()).orElseThrow(CommentNotFoundException::new);
            UserEntity user = CurrentUser.getCurrentUserProfile();
            commentEntity.setPost(post);
            commentEntity.setUser(user);
            commentEntity.setContent(comment.getContent());
        }else{
            commentEntity = commentJpaRepository.findById(comment.getCommentId()).orElseThrow(CommentNotFoundException::new);
            commentEntity.mergeDomain(comment);
        }
        CommentEntity savedComment = commentJpaRepository.save(commentEntity);
        return commentCommentEntityAdapter.convertCommentEntityToComment(savedComment);
    }

    @Override
    public Page<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable) {
        return commentJpaRepository.findByParentIdOrderByCreatedAtDesc(postId, pageable).map(commentCommentEntityAdapter::convertCommentEntityToComment);
    }

    @Override
    public Page<Comment> findByPostIdAndParentId(Pageable pageable, Long postId, Long parentId) {
        return null;
    }

    @Override
    public void delete(Comment comment) {
        CommentEntity commentEntity = commentJpaRepository.findById(comment.getCommentId()).orElseThrow(CommentNotFoundException::new);
        commentEntity.markAsDeleted();
        commentJpaRepository.save(commentEntity);
    }
}
