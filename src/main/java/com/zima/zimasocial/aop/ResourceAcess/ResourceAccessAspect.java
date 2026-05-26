package com.zima.zimasocial.aop.ResourceAcess;

import com.zima.zimasocial.config.CustomUserDetails;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.domain.value.CommentId;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import com.zima.zimasocial.context.social2.repository.AuthorRepository;
import com.zima.zimasocial.context.social2.repository.CommentRepository;
import com.zima.zimasocial.context.social2.repository.PostRepository;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.exception.DataNotFoundException;
import com.zima.zimasocial.exception.UnauthorizedException;
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

    @Autowired
    private AuthorRepository authorRepository;

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
        Post post = postRepository.findById(new PostId(postId)).orElseThrow(()-> new DataNotFoundException("Post not found"));
        if(!post.getAuthor().getId().getValue().equals(currentUser.getId())){
            throw new UnauthorizedException("You have no access to this resource");
        }
    }
    private void validateCommentAccess(Long commentId){
        Author currentUser = authorRepository.getAuthenticatedAuthor();
        Comment comment = commentRepository.findById(new CommentId(commentId)).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        if(!comment.getAuthor().equals(currentUser)){
            throw new UnauthorizedException("You have no access to this resource");
        }
    }
}
