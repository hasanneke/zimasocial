package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.todayspost.TodaysPostDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TodaysPostRepositoryDomain extends JpaRepository<TodaysPostDomain, Long> {
    @Query("""
        SELECT todaysPost FROM TodaysPostDomain todaysPost
        WHERE todaysPost.author.isBanned = false
        AND todaysPost.author.isDisabled = false
        AND todaysPost.author.isPrivate = false 
        AND todaysPost.author.isDeleted = false
        AND todaysPost.date = :date
        """)
    List<TodaysPostDomain> findTodaysPostByDate(LocalDate date);
}
