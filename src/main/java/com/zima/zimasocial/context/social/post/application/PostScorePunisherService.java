package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social.post.repository.PostDomainRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostScorePunisherService {
    private final PostDomainRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    public void punishPosts() {
        logger.info("--- Punishing post started ----");
        LocalDateTime start = LocalDate.now().minusDays(3).atStartOfDay();
        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay();
        List<PostDomain> posts = postRepository.findAllByCreatedAtBetween(start, end);
        logger.info("--- %d posts will be punished ----".formatted(posts.size()));
        for (PostDomain post : posts) {
            post.punishScore();
            postRepository.save(post);
        }
        logger.info("--- %d posts are successfully punished ----".formatted(posts.size()));
    }
}
