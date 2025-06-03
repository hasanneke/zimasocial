package com.zimaberlin.zimasocial.factory;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.report.ReportId;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.LikeRepository;
import com.zimaberlin.zimasocial.repository.ReportRepository;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.UserViewFactory;
import com.zimaberlin.zimasocial.views.comment.CommentView;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentViewFactory {
    private final UserViewFactory userMapper;
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;
    public CommentView populated(CommentEntity entity) {
        // Create domain instance
        CommentView commentView = new CommentView();
        // UnProxy Proxies
        UserEntity user = (UserEntity) Hibernate.unproxy(entity.getUser());
        // Set domain values
        commentView.setContent(entity.getContent());
        commentView.setId(entity.getId());
        commentView.setUserView(userMapper.populated(user));
        commentView.setUpdatedAt(entity.getUpdatedAt());
        commentView.setCreatedAt(entity.getCreatedAt());
        commentView.setLikeCount(entity.getLikeCount());
        commentView.setReplyCount(entity.getReplyCount());

        Optional<LikeEntity> like = likeRepository.findByUserAndComment(CurrentUser.getCurrentUserProfile(), entity);
        commentView.setIsLiked(like.isPresent());

        Optional<ReportEntity> report = reportRepository.findById(new ReportId(entity.getId(), CurrentUser.getCurrentUserProfile().getId(), ResourceType.comment));
        commentView.setIsReported(report.isPresent());
        return commentView;
    }
    public List<CommentView> populated(List<CommentEntity> comments) {
        return comments.stream().map(this::populated).toList();
    }

    public CommentView plain(CommentEntity entity) {
        CommentView commentView = new CommentView();
        // UnProxy Proxies
        UserEntity user = (UserEntity) Hibernate.unproxy(entity.getUser());
        // Set domain values
        commentView.setContent(entity.getContent());
        commentView.setId(entity.getId());
        commentView.setUserView(userMapper.populated(user));
        commentView.setUpdatedAt(entity.getUpdatedAt());
        commentView.setCreatedAt(entity.getCreatedAt());
        commentView.setLikeCount(entity.getLikeCount());
        commentView.setReplyCount(entity.getReplyCount());
        return commentView;
    }
}
