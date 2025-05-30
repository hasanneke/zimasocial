package com.zimaberlin.zimasocial.calculators.todayspost;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.views.post.PostView;

import java.util.List;

public interface TodaysPostGenerator {
    void createTodaysPost();
    List<TodaysPost> selectTodaysPosts();
}
