package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.post.entity.TodaysPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodaysPostRepository extends JpaRepository<TodaysPost, Long> {
    List<TodaysPost> findAllByDate(LocalDate date);
}
