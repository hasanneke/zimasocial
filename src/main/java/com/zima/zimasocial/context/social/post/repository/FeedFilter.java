package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedFilter {
    Integer offset;
    Integer size;
    Integer lastScore;
    Long lastId;
    PostCategory category;
    PostSortType sortType;
    AuthorId ownerAuthorId;
    AuthorId readerAuthorId;
}
