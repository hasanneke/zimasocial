package com.zima.zimasocial.utility;

import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.entity.PostJpaEntity;
import com.zima.zimasocial.entity.report.ReportEntity;
import com.zima.zimasocial.entity.report.ReportId;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.LikeJpaRepository;
import com.zima.zimasocial.repository.ReportJpaRepository;
import com.zima.zimasocial.context.social2.api.views.PostView;
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

    public PostView populated(PostJpaEntity postJpaEntity) {
        UserEntity profile = CurrentUser.getCurrentUserProfile();
        Optional<ReportEntity> report = reportRepository.findById(new ReportId(postJpaEntity.getId(), profile.getId(), ResourceType.post));
        Optional<LikeEntity> like = likeJpaRepository.findByUserIdAndPostIdAndType(profile.getId(), postJpaEntity.getId(), LikeType.post);

        PostView postView = new PostView();
        if(like.isPresent()){
            postView.setLiked(true);;
        }
        UserEntity user = Hibernate.unproxy(postJpaEntity.getUser(), UserEntity.class);
        postView.setIsReported(report.isPresent());
        postView.setUser(userMapper.populated(user));
        postView.setId( postJpaEntity.getId() );
        postView.setContent( postJpaEntity.getContent() );
        postView.setType( postJpaEntity.getType() );
        postView.setLikeCount( postJpaEntity.getLikeCount() );
        postView.setCommentCount( postJpaEntity.getCommentCount() );
        postView.setCreatedAt( postJpaEntity.getCreatedAt() );
        postView.setUpdatedAt( postJpaEntity.getUpdatedAt() );

        return postView;
    }

    public List<PostView> populated(List<PostJpaEntity> entities) {
        return entities.stream().map(this::populated).toList();
    }
}
