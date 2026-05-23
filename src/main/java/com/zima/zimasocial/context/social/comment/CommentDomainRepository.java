package com.zima.zimasocial.context.social.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentDomainRepository {
    Optional<CommentDomain> findById(Long id);
    CommentDomain save(CommentDomain comment);
    void saveAll(List<CommentDomain> comments);
    Page<CommentDomain> findByParentIdOrderByCreatedAt(Long parentId, Pageable pageable);
    Page<CommentDomain> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    void delete(CommentDomain comment);
}
