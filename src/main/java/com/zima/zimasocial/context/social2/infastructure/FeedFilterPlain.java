package com.zima.zimasocial.context.social2.infastructure;

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
