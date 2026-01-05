//package com.zima.zimasocial.service.notification;
//
//import com.zima.zimasocial.entity.*;
//import com.zima.zimasocial.entity.user.UserEntity;
//import com.zima.zimasocial.repository.NotificationJpaRepository;
//import com.zima.zimasocial.repository.PostJpaRepository;
//import com.zima.zimasocial.repository.UserJpaRepository;
//import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
//import com.zima.zimasocial.service.users.exception.UserNotFoundException;
//import com.zima.zimasocial.utility.CurrentUser;
//import com.zima.zimasocial.utility.UserViewFactory;
//import com.zima.zimasocial.views.notification.NotificationView;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//import static com.zima.zimasocial.utility.CurrentUser.getCurrentUserProfile;
//
//@Service
//public class NotificationServiceImpl implements NotificationService{
//    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
//    private final NotificationJpaRepository notificationRepository;
//    private final PostJpaRepository postJpaRepository;
//    private final UserJpaRepository userRepository;
//    private final UserViewFactory userMapper;
//
//    @Autowired
//    public NotificationServiceImpl(NotificationJpaRepository notificationRepository, UserViewFactory userMapper, PostJpaRepository postJpaRepository, UserJpaRepository userRepository) {
//        this.notificationRepository = notificationRepository;
//        this.userMapper = userMapper;
//        this.postJpaRepository = postJpaRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public Page<NotificationView> getNotifications(Long userId, Pageable page) {
//        Page<NotificationEntity> notificationEntities = notificationRepository.findByReceiverUserIdOrderByCreatedAtDesc(userId, page);
//        return notificationEntities.map((e)->NotificationView.builder()
//                .id(e.getId())
//                .url(e.getUrl())
//                .type(e.getType())
//                .content(e.getContent())
//                .targetId(e.getTargetId())
//                .targetCollection(e.getTargetCollection())
//                .postId(e.getPostId())
//                .createdAt(e.getCreatedAt())
//                .actor(userMapper.populated(e.getActor()))
//                .build());
//    }
//
//    @Override
//    public void sendPostLikedNotification(LikeEntity like) {
//        UserEntity actor = getCurrentUserProfile();
//        PostEntity post = postJpaRepository.findById(like.getPostId()).orElseThrow(PostNotFoundException::new);
//        UserEntity receiver = userRepository.findById(post.getUser().getId()).orElseThrow(UserNotFoundException::new);
//        Optional<NotificationEntity> notificationEntity =
//                notificationRepository.findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(receiver, actor, like.getPostId(), NotificationType.POST_LIKED, TargetCollection.post);
//        if(notificationEntity.isPresent()){
//            return;
//        }
//
//        logger.info(String.format("Post %d liked", like.getPostId()));
//        String message = actor.getName() + " paylaşımını beğendi";
//
//        NotificationEntity notification = NotificationEntity.builder()
//                .type(NotificationType.POST_LIKED)
//                .actor(actor)
//                .receiverUser(receiver)
//                .targetCollection(TargetCollection.post)
//                .targetId(like.getPostId())
//                .postId(like.getPostId())
//                .content(message)
//                .build();
//
//        notificationRepository.save(notification);
//    }
//
//    @Override
//    public void sendPostCommentedNotification(CommentEntity comment) {
//        logger.info(String.format("Post %d commented", comment.getPost().getId()));
//        UserEntity actor = getCurrentUserProfile();
//        UserEntity receiver = comment.getPost().getUser();
//        String message = actor.getName() + " paylaşımına yorum yaptı";
//        NotificationEntity notification = NotificationEntity.builder()
//                .type(NotificationType.POST_COMMENTED)
//                .actor(actor)
//                .receiverUser(receiver)
//                .targetCollection(TargetCollection.comment)
//                .targetId(comment.getId())
//                .postId(comment.getPost().getId())
//                .content(message)
//                .build();
//        notificationRepository.save(notification);
//    }
//
//    @Override
//    public void sendCommentLikedNotification(CommentEntity comment) {
//        logger.info(String.format("Comment %d liked", comment.getId()));
//        UserEntity receiver = comment.getUser();
//        UserEntity actor = getCurrentUserProfile();
//        String message = actor.getName() + " yorumunu beğendi";
//        Optional<NotificationEntity> notificationEntity =
//                notificationRepository.findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(receiver, actor, comment.getId(), NotificationType.COMMENT_LIKED, TargetCollection.comment);
//        if(notificationEntity.isPresent()){
//            return;
//        }
//        NotificationEntity notification = NotificationEntity.builder()
//                .type(NotificationType.COMMENT_LIKED)
//                .actor(actor)
//                .receiverUser(receiver)
//                .targetCollection(TargetCollection.comment)
//                .targetId(comment.getId())
//                .postId(comment.getPost().getId())
//                .content(message)
//                .build();
//        notificationRepository.save(notification);
//    }
//
//    @Override
//    public void sendCommentRepliedNotification(CommentEntity reply) {
//        logger.info(String.format("Comment %d replied", reply.getId()));
//
//        UserEntity commentOwner = reply.getParent().getUser();
//        UserEntity commenter = getCurrentUserProfile();
//        String message = commenter.getName() + " yorumuna yanıt verdi";
//
//        NotificationEntity notificationEntity = NotificationEntity.builder()
//                .type(NotificationType.COMMENT_REPLIED)
//                .actor(commenter)
//                .receiverUser(commentOwner)
//                .targetCollection(TargetCollection.comment)
//                .targetId(reply.getId())
//                .postId(reply.getPost().getId())
//                .content(message)
//                .build();
//
//        notificationRepository.save(notificationEntity);
//    }
//
//    @Override
//    public void removePostLikedNotification(PostEntity post) {
//        UserEntity actor = CurrentUser.getCurrentUserProfile();
//        Optional<NotificationEntity> notificationEntity =
//                notificationRepository.findByReceiverUserAndActorAndPostIdAndTypeAndTargetCollection(post.getUser(), actor, post.getId(), NotificationType.POST_LIKED, TargetCollection.post);
//        if(notificationEntity.isPresent()){
//            notificationEntity.get().markAsDeleted();
//            notificationRepository.save(notificationEntity.get());
//        }
//    }
//
//    @Override
//    public void removePostCommentedNotification(CommentEntity comment) {
//        UserEntity actor = CurrentUser.getCurrentUserProfile();
//        Optional<NotificationEntity> notificationEntity =
//                notificationRepository.findByReceiverUserAndActorAndPostIdAndTypeAndTargetCollection(comment.getUser(), actor, comment.getId(), NotificationType.POST_COMMENTED, TargetCollection.comment);
//        if(notificationEntity.isPresent()){
//            notificationEntity.get().markAsDeleted();
//            notificationRepository.save(notificationEntity.get());
//        }
//    }
//
//    @Override
//    public void removeCommentLikedNotification(CommentEntity comment) {
//        UserEntity actor = CurrentUser.getCurrentUserProfile();
//        Optional<NotificationEntity> notificationEntity =
//                notificationRepository.findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(comment.getUser(), actor, comment.getId(), NotificationType.COMMENT_LIKED, TargetCollection.comment);
//        if(notificationEntity.isPresent()){
//            notificationEntity.get().markAsDeleted();
//            notificationRepository.save(notificationEntity.get());
//        }
//    }
//
//    @Override
//    public void removeCommentRepliedNotification(CommentEntity reply) {
//        UserEntity actor = CurrentUser.getCurrentUserProfile();
//        Optional<NotificationEntity> notificationEntity =
//                notificationRepository.findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(reply.getUser(), actor, reply.getId(), NotificationType.COMMENT_LIKED, TargetCollection.comment);
//        if(notificationEntity.isPresent()){
//            notificationEntity.get().markAsDeleted();
//            notificationRepository.save(notificationEntity.get());
//        }
//    }
//
//    @Override
//    public void removeCommentReplyLikedNotification(CommentEntity reply) {
//        UserEntity actor = CurrentUser.getCurrentUserProfile();
//        Optional<NotificationEntity> notificationEntity =
//                notificationRepository.findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(reply.getUser(), actor, reply.getId(), NotificationType.COMMENT_REPLIED, TargetCollection.comment);
//        if(notificationEntity.isPresent()){
//            notificationEntity.get().markAsDeleted();
//            notificationRepository.save(notificationEntity.get());
//        }
//    }
//}
