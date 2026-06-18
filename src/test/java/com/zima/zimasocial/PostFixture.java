package com.zima.zimasocial;

import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.value.PostId;
import com.zima.zimasocial.context.social.media.value.MediaType;

import java.util.Random;

public class PostFixture {
    private PostFixture() {}

    public static Post validPost() {
        return Post.create(
                new PostId(new Random().nextLong()),
                new PostContent("", MediaType.any),
                new AuthorId(new Random().nextLong()),
                null
        );
    }
}
