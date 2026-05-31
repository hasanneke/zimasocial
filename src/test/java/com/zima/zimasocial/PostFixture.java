package com.zima.zimasocial;

import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.domain.value.PostContent;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import com.zima.zimasocial.entity.MediaType;

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
