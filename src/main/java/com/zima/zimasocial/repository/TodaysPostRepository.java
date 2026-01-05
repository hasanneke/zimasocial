package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.todayspost.TodaysPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodaysPostRepository extends JpaRepository<TodaysPost, Long> {
    List<TodaysPost> findTodaysPostByDate(LocalDate date);
}
