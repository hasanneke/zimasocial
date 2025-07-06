package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.entity.PostType;
import lombok.Builder;

@Builder
public record CreatePost(PostType type, String content, Integer mediaId, String language) { }
