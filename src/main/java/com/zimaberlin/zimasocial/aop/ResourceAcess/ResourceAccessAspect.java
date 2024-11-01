package com.zimaberlin.zimasocial.aop.ResourceAcess;

import com.zimaberlin.zimasocial.config.CustomUserDetails;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.exception.UnauthorizedException;
import com.zimaberlin.zimasocial.repository.CommentRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.utility.CurrentUser;
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
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

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
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post not found"));
        if(!postEntity.getUser().equals(currentUser)){
            throw new UnauthorizedException("You have no access to this resource");
        }
    }
    private void validateCommentAccess(Long commentId){
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        if(!comment.getUser().equals(currentUser)){
            throw new UnauthorizedException("You have no access to this resource");
        }
    }
}
