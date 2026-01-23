package com.zima.zimasocial.context.social.api;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import com.zima.zimasocial.context.social.post.repository.PostSortType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedFilterPlain {
    Integer size;
    Integer lastScore;
    Long lastPostId;
    PostCategory category;
    PostSortType sortType;
    String slug;
}
