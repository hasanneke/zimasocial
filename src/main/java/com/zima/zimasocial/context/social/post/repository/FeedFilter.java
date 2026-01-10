package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedFilter {
    Integer size;
    Integer lastScore;
    Long lastId;
    Long userId;
    PostCategory category;
    PostSortType sortType;
}
