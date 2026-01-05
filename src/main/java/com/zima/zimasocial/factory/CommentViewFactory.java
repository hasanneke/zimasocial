package com.zima.zimasocial.factory;

import com.zima.zimasocial.entity.CommentEntity;
import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.entity.report.ReportEntity;
import com.zima.zimasocial.entity.report.ReportId;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.LikeJpaRepository;
import com.zima.zimasocial.repository.ReportJpaRepository;
import com.zima.zimasocial.utility.CurrentUser;
import com.zima.zimasocial.utility.UserViewFactory;
import com.zima.zimasocial.views.comment.CommentView;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentViewFactory {
    private final UserViewFactory userMapper;
    private final LikeJpaRepository likeJpaRepository;
    private final ReportJpaRepository reportRepository;
    public CommentView populated(CommentEntity entity) {
        // Create domain instance
        CommentView commentView = new CommentView();
        // UnProxy Proxies
        UserEntity user = (UserEntity) Hibernate.unproxy(entity.getUser());
        // Set domain values
        commentView.setContent(entity.getContent());
        commentView.setId(entity.getId());
        commentView.setAuthor(userMapper.populated(user));
        commentView.setUpdatedAt(entity.getUpdatedAt());
        commentView.setCreatedAt(entity.getCreatedAt());
        commentView.setLikeCount(entity.getLikeCount());
        commentView.setReplyCount(entity.getReplyCount());

        Optional<LikeEntity> like = likeJpaRepository.findByUserIdAndCommentId(CurrentUser.getCurrentUserProfile().getId(), entity.getId());
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
        commentView.setAuthor(userMapper.populated(user));
        commentView.setUpdatedAt(entity.getUpdatedAt());
        commentView.setCreatedAt(entity.getCreatedAt());
        commentView.setLikeCount(entity.getLikeCount());
        commentView.setReplyCount(entity.getReplyCount());
        return commentView;
    }
}
