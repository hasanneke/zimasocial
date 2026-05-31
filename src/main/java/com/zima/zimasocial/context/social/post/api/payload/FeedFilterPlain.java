package com.zima.zimasocial.context.social.post.api.payload;

import com.zima.zimasocial.context.social.post.value.PostCategory;
import com.zima.zimasocial.context.social.post.value.PostSortType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedFilterPlain {
    Integer offset;
    Integer size;
    Integer lastScore;
    Long lastPostId;
    PostCategory category;
    PostSortType sortType;
    String slug;
}
