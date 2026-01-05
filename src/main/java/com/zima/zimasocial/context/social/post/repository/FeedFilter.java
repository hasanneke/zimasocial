package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.entity.MediaType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedFilter {
    Integer size;
    Integer lastScore;
    LocalDateTime lastCreatedAt;
    Long lastId;
    Long userId;
    MediaType type;
}
