package com.zima.zimasocial.context.social2.application;

import com.zima.zimasocial.context.social2.domain.entity.TodaysPost;

import java.util.List;

public interface TodaysPostGenerator {
    void createTodaysPost();
    List<TodaysPost> selectTodaysPosts();
}
