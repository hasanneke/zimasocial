package com.zimaberlin.zimasocial.context.social.post.value;

import com.zimaberlin.zimasocial.entity.MediaType;
import lombok.Builder;

@Builder
public record CreatePost(MediaType type, String content, String mediaId) { }
