package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.LikeType;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.report.ReportId;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.LikeJpaRepository;
import com.zimaberlin.zimasocial.repository.ReportJpaRepository;
import com.zimaberlin.zimasocial.views.post.PostView;
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
