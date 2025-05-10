package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.views.post.PostView;
import org.hibernate.Hibernate;

public class CustomPostMapper {
    public static PostView postEntityToPost(PostEntity postEntity) {
        if ( postEntity == null ) {
            return null;
        }

        PostView postView = new PostView();

        UserEntity user = Hibernate.unproxy(postEntity.getUser(), UserEntity.class);
        postView.setUser(CustomUserMapper.entityToDomain(user));
        postView.setId( postEntity.getId() );
        postView.setContent( postEntity.getContent() );
        postView.setUrl( postEntity.getUrl() );
        postView.setType( postEntity.getType() );
        postView.setLikeCount( postEntity.getLikeCount() );
        postView.setCommentCount( postEntity.getCommentCount() );
        postView.setCreatedAt( postEntity.getCreatedAt() );
        postView.setUpdatedAt( postEntity.getUpdatedAt() );
        postView.addLinks();

        return postView;
    }

    public static PostEntity payloadToPostEntity(PostPayload payload) {
        if ( payload == null ) {
            return null;
        }

        PostEntity postEntity = new PostEntity();

        postEntity.setContent( payload.getContent() );
        postEntity.setUrl( payload.getUrl() );
        postEntity.setType( payload.getType() );

        return postEntity;
    }
}
