package com.zima.zimasocial.context.social.post.value;

import com.zima.zimasocial.entity.MediaType;
import lombok.Builder;

@Builder
public record CreatePost(MediaType type, String content, String mediaId) { }
