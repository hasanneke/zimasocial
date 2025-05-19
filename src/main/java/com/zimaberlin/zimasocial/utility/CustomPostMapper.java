package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.LikeRepository;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.views.post.PostView;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomPostMapper {
    private final CustomUserMapper userMapper;
    private final LikeRepository likeRepository;

    @Autowired
    public CustomPostMapper(CustomUserMapper userMapper, LikeRepository likeRepository) {
        this.userMapper = userMapper;
        this.likeRepository = likeRepository;
    }

    public PostView postEntityToPost(PostEntity postEntity) {
        if ( postEntity == null ) {
            return null;
        }

        PostView postView = new PostView();
        UserEntity profile = CurrentUser.getCurrentUserProfile();
        Optional<LikeEntity> like = likeRepository.findByUserAndPost(profile, postEntity);
        if(like.isPresent()){
            postView.setLiked(true);;

        }
        UserEntity user = Hibernate.unproxy(postEntity.getUser(), UserEntity.class);
        postView.setUser(userMapper.entityToDomain(user));
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
