package com.zima.zimasocial.context.social.author.abstracted;

import com.zima.zimasocial.context.social.post.entity.TodaysPost;

import java.util.List;

public interface TodaysPostGenerator {
    void createTodaysPost();
    List<TodaysPost> selectTodaysPosts();
}
