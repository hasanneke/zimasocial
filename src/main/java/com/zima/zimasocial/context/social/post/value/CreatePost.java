package com.zima.zimasocial.context.social.post.value;

import com.zima.zimasocial.context.social.media.value.MediaType;
import lombok.Builder;

@Builder
public record CreatePost(MediaType type, String content, String mediaId) { }
