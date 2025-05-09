//package com.zimaberlin.zimasocial.events.Listeners;
//
//import com.zimaberlin.zimasocial.controller.PostController;
//import com.zimaberlin.zimasocial.entity.*;
//import com.zimaberlin.zimasocial.events.*;
//import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
//import com.zimaberlin.zimasocial.repository.NotificationRepository;
//import com.zimaberlin.zimasocial.repository.UserRepository;
//import com.zimaberlin.zimasocial.utility.CurrentUser;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class PostEventListener {
//    @Autowired
//    private final NotificationRepository notificationRepository;
//    @Autowired
//    private final UserRepository userRepository;
//    Logger logger = LoggerFactory.getLogger(PostEventListener.class);
//
//    @PersistenceContext
//    private final EntityManager entityManager;
//
//    @EventListener
//    @Transactional
//    public void handlePostLikedEvent(PostLikedEvent event){
//        logger.info(String.format("Post %d liked", event.getPost().getId()));
//        UserEntity postOwner = event.getPost().getUser();
//        UserEntity liker = getCurrentUserProfile();
//
//        NotificationEntity notificationEntity = new NotificationEntity();
//        notificationEntity.setType(NotificationType.POST_LIKED);
//        notificationEntity.setSenderUser(liker);
//        notificationEntity.setReceiverUser(postOwner);
//
//        notificationEntity.setTargetCollection(TargetCollection.post);
//        notificationEntity.setTargetId(event.getPost().getId());
//        notificationEntity.setPost(event.getPost());
//
//        String message = liker.getName() + " paylaşımını beğendi";
//        notificationEntity.setContent(message);
//        notificationRepository.save(notificationEntity);
//    }
//
//    @EventListener
//    @Transactional
//    public void handleCommentLikedEvent(CommentLikedEvent event) throws NoSuchMethodException {
//        logger.info(String.format("Comment %d liked", event.getComment().getId()));
//
//        UserEntity postOwner = event.getComment().getUser();
//        UserEntity liker = getCurrentUserProfile();
//
//        NotificationEntity notificationEntity = new NotificationEntity();
//        notificationEntity.setType(NotificationType.COMMENT_LIKED);
//        notificationEntity.setSenderUser(liker);
//        notificationEntity.setReceiverUser(postOwner);
//
//        notificationEntity.setTargetCollection(TargetCollection.comment);
//        notificationEntity.setTargetId(event.getComment().getId());
//        notificationEntity.setPost(event.getComment().getPost());
//
//        String message = liker.getName() + " yorumunu beğendi";
//        notificationEntity.setContent(message);
//        notificationRepository.save(notificationEntity);
//    }
//
//    @EventListener
//    @Transactional
//    public void handlePostCommentedEvent(PostCommentedEvent event){
//        logger.info(String.format("Post %d commented", event.getPost().getId()));
//
//        UserEntity postOwner = event.getPost().getUser();
//        UserEntity commenter = getCurrentUserProfile();
//
//        NotificationEntity notificationEntity = new NotificationEntity();
//        notificationEntity.setType(NotificationType.POST_COMMENTED);
//        notificationEntity.setSenderUser(commenter);
//        notificationEntity.setReceiverUser(postOwner);
//
//        notificationEntity.setTargetCollection(TargetCollection.comment);
//        notificationEntity.setTargetId(event.getComment().getId());
//        notificationEntity.setPost(event.getPost());
//
//        String message = commenter.getName() + " paylaşımına yorum yaptı";
//
//        notificationEntity.setContent(message);
//        notificationRepository.save(notificationEntity);
//    }
//    @EventListener
//    @Transactional
//    public void handleCommentRepliedEvent(CommentRepliedEvent event){
//        logger.info(String.format("Comment %d replied", event.getComment().getId()));
//
//        UserEntity postOwner = event.getComment().getUser();
//        UserEntity commenter = getCurrentUserProfile();
//
//        NotificationEntity notificationEntity = new NotificationEntity();
//        notificationEntity.setType(NotificationType.POST_COMMENTED);
//        notificationEntity.setSenderUser(commenter);
//        notificationEntity.setReceiverUser(postOwner);
//
//        notificationEntity.setTargetCollection(TargetCollection.comment);
//        notificationEntity.setTargetId(event.getComment().getId());
//        notificationEntity.setPost(event.getComment().getPost());
//
//        String message = commenter.getName() + "yorumuna yanıt verdi";
//
//        notificationEntity.setContent(message);
//        notificationRepository.save(notificationEntity);
//    }
//
//    @EventListener
//    @Transactional
//    public void handlePostUnlikedEvent(PostUnlikedEvent event){
//        logger.info(String.format("Post %d unliked", event.getPost().getId()));
//    }
//
//    private UserEntity getCurrentUserProfile(){
//        UserEntity userObject = CurrentUser.getCurrentUserProfile();
//
//        return userRepository.findById(userObject.getId()).orElseThrow(()->new ResourceNotFoundException("User not found"));
//    }
//}
