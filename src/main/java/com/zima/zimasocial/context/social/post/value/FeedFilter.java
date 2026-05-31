package com.zima.zimasocial.context.social.post.value;

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
