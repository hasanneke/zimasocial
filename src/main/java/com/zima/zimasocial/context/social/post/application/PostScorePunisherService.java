package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
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
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    public void punishPosts() {
        logger.info("--- Punishing post started ----");
        LocalDateTime start = LocalDate.now().minusDays(3).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();
        List<Post> posts = postRepository.findAllByCreatedAtBetween(start, end);
        logger.info("--- %d posts will be punished ----".formatted(posts.size()));
        for (Post post : posts) {
            post.punishScore();
        }
        postRepository.saveAll(posts);
        logger.info("--- %d posts are successfully punished ----".formatted(posts.size()));
    }
}
