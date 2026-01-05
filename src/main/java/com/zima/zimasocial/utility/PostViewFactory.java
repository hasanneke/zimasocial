package com.zima.zimasocial.utility;

import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.entity.PostEntity;
import com.zima.zimasocial.entity.report.ReportEntity;
import com.zima.zimasocial.entity.report.ReportId;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.LikeJpaRepository;
import com.zima.zimasocial.repository.ReportJpaRepository;
import com.zima.zimasocial.views.post.PostView;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostViewFactory {
    private final UserViewFactory userMapper;
    private final LikeJpaRepository likeJpaRepository;
    private final ReportJpaRepository reportRepository;

    @Autowired
    public PostViewFactory(UserViewFactory userMapper, LikeJpaRepository likeJpaRepository, ReportJpaRepository reportRepository) {
        this.userMapper = userMapper;
        this.likeJpaRepository = likeJpaRepository;
        this.reportRepository = reportRepository;
    }

    public PostView populated(PostEntity postEntity) {
        UserEntity profile = CurrentUser.getCurrentUserProfile();
        Optional<ReportEntity> report = reportRepository.findById(new ReportId(postEntity.getId(), profile.getId(), ResourceType.post));
        Optional<LikeEntity> like = likeJpaRepository.findByUserIdAndPostIdAndType(profile.getId(), postEntity.getId(), LikeType.post);

        PostView postView = new PostView();
        if(like.isPresent()){
            postView.setLiked(true);;
        }
        UserEntity user = Hibernate.unproxy(postEntity.getUser(), UserEntity.class);
        postView.setIsReported(report.isPresent());
        postView.setUser(userMapper.populated(user));
        postView.setId( postEntity.getId() );
        postView.setContent( postEntity.getContent() );
        postView.setType( postEntity.getType() );
        postView.setLikeCount( postEntity.getLikeCount() );
        postView.setCommentCount( postEntity.getCommentCount() );
        postView.setCreatedAt( postEntity.getCreatedAt() );
        postView.setUpdatedAt( postEntity.getUpdatedAt() );

        return postView;
    }

    public List<PostView> populated(List<PostEntity> entities) {
        return entities.stream().map(this::populated).toList();
    }
}
