package com.zima.zimasocial.calculators.todayspost;

import com.zima.zimasocial.entity.todayspost.TodaysPost;

import java.util.List;

public interface TodaysPostGenerator {
    void createTodaysPost();
    List<TodaysPost> selectTodaysPosts();
}
