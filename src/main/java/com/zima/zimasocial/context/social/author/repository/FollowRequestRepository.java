package com.zima.zimasocial.context.social.author.repository;

import com.zima.zimasocial.context.social.author.entity.FollowRequest;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    Optional<FollowRequest> findByFollowerIdAndFollowedId(AuthorId followerId, AuthorId followedId);
    List<FollowRequest> findAllByFollowedId(AuthorId followedId);
}
