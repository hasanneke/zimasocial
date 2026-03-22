package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.todayspost.TodaysPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TodaysPostRepository extends JpaRepository<TodaysPost, Long> {
    @Query("""
        SELECT todaysPost FROM TodaysPost todaysPost
        WHERE todaysPost.author.isBanned = false
        AND todaysPost.author.isDisabled = false
        AND todaysPost.author.isPrivate = false 
        AND todaysPost.author.isDeleted = false
        AND todaysPost.date = :date
        """)
    List<TodaysPost> findTodaysPostByDate(LocalDate date);
}
