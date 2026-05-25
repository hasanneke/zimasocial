package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, PostId> {
    @Query(value = "SELECT nextval('post_sequence')", nativeQuery = true)
    Long getNextSequenceValue();

    List<Post> findAllByAuthorId(AuthorId authorId);

    default PostId getNextSequence() {
        return new PostId(getNextSequenceValue());
    }

    void deleteAllByAuthorId(AuthorId authorId);

    List<Post> findAllByIsVisibleFalseAndAuthorId(AuthorId authorId);
}
