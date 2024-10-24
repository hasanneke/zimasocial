package com.zimaberlin.zimasocial.aop.ResourceAcess;

import com.zimaberlin.zimasocial.dto.CustomUserDetails;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.Profile;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.exception.UnauthorizedException;
import com.zimaberlin.zimasocial.repository.PostRepository;
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

    private void validatePostAccess(Long id){
        Profile currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        PostEntity postEntity = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post not found"));
        if(!postEntity.getUser().equals(currentUser)){
            throw new UnauthorizedException("You have no access to this resource");
        }
    }
}
