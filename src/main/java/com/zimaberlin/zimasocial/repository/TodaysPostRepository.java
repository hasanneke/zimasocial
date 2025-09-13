package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodaysPostRepository extends JpaRepository<TodaysPost, Long> {
    List<TodaysPost> findTodaysPostByDate(LocalDate date);
}
