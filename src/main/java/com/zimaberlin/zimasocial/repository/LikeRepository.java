package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.Like;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(Profile user, PostEntity postEntity);
}
