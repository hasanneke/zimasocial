package com.zimaberlin.zimasocial.events.Listeners;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.events.PostLikedEvent;
import com.zimaberlin.zimasocial.events.PostUnlikedEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostEventListener {

    Logger logger = LoggerFactory.getLogger(PostEventListener.class);

    @PersistenceContext
    private final EntityManager entityManager;

    @EventListener
    @Transactional
    public void handlePostLikedEvent(PostLikedEvent event){
        logger.info(String.format("Post %d liked", event.getPost().getId()));
    }

    @EventListener
    @Transactional
    public void handlePostUnlikedEvent(PostUnlikedEvent event){
        logger.info(String.format("Post %d unliked", event.getPost().getId()));
    }
}
