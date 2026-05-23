package com.zima.zimasocial.aop.ResourceAcess;

import com.zima.zimasocial.config.CustomUserDetails;
import com.zima.zimasocial.entity.CommentEntity;
import com.zima.zimasocial.entity.PostJpaEntity;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.exception.DataNotFoundException;
import com.zima.zimasocial.exception.UnauthorizedException;
import com.zima.zimasocial.repository.CommentJpaRepository;
import com.zima.zimasocial.repository.PostJpaRepository;
import com.zima.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ResourceAccessAspect {
    private final Logger logger = LoggerFactory.getLogger(ResourceAccessAspect.class);

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Before("@annotation(hasPostAccess)")
    public void checkPostAccess(JoinPoint joinPoint, HasPostAccess hasPostAccess) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        String targetParam = hasPostAccess.idParameterName();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(targetParam) ) {
                Long postId = (Long) args[i];
                validatePostAccess(postId);
                return;
            }
        }
        throw new IllegalArgumentException("Parameter " + targetParam + " not found");
    }

    @Before("@annotation(hasCommentAccess)")
    public void checkCommentAccess(JoinPoint joinPoint, HasCommentAccess hasCommentAccess) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        String targetParam = hasCommentAccess.idParameterName();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(targetParam) ) {
                Long commentId = (Long) args[i];
                validateCommentAccess(commentId);
                return;
            }
        }
        throw new IllegalArgumentException("Parameter " + targetParam + " not found");
    }

    private void validatePostAccess(Long postId){
        UserEntity currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        PostJpaEntity postJpaEntity = postJpaRepository.findById(postId).orElseThrow(()-> new DataNotFoundException("Post not found"));
        if(!postJpaEntity.getUser().equals(currentUser)){
            throw new UnauthorizedException("You have no access to this resource");
        }
    }
    private void validateCommentAccess(Long commentId){
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();
        CommentEntity comment = commentJpaRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        if(!comment.getUser().equals(currentUser)){
            throw new UnauthorizedException("You have no access to this resource");
        }
    }
}
