package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.report.ReportId;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.LikeRepository;
import com.zimaberlin.zimasocial.repository.ReportRepository;
import com.zimaberlin.zimasocial.views.post.PostView;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostViewFactory {
    private final UserViewFactory userMapper;
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;

    @Autowired
    public PostViewFactory(UserViewFactory userMapper, LikeRepository likeRepository, ReportRepository reportRepository) {
        this.userMapper = userMapper;
        this.likeRepository = likeRepository;
        this.reportRepository = reportRepository;
    }

    public PostView populated(PostEntity postEntity) {
        UserEntity profile = CurrentUser.getCurrentUserProfile();
        Optional<ReportEntity> report = reportRepository.findById(new ReportId(postEntity.getId(), profile.getId(), ResourceType.post));
        Optional<LikeEntity> like = likeRepository.findByUserAndPost(profile, postEntity);

        PostView postView = new PostView();
        if(like.isPresent()){
            postView.setLiked(true);;
        }
        UserEntity user = Hibernate.unproxy(postEntity.getUser(), UserEntity.class);
        postView.setIsReported(report.isPresent());
        postView.setUser(userMapper.populated(user));
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

    public List<PostView> populated(List<PostEntity> entities) {
        return entities.stream().map(this::populated).toList();
    }
}
