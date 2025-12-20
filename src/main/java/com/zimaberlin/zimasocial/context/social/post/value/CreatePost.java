package com.zimaberlin.zimasocial.context.social.post.value;

import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.entity.PostType;
import lombok.Builder;

@Builder
public record CreatePost(PostType type, String content, String mediaId, String language, MovieMediaType movieMediaType) { }
