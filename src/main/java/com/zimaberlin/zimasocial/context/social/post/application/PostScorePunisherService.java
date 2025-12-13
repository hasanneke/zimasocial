package com.zimaberlin.zimasocial.context.social.post.application;

import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import com.zimaberlin.zimasocial.context.social.post.repository.PostRepository;
import com.zimaberlin.zimasocial.context.social.post.service.PostScoreReducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostScorePunisherService {
    private final PostRepository postRepository;
    private final PostScoreReducer postScoreReducer;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    public void punishPosts() {
        logger.info("--- Punishing post started ----");
        OffsetDateTime start = LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
        OffsetDateTime end = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
        List<Post> posts = postRepository.findAllByCreatedAtBetween(start, end);
        logger.info("--- %d posts will be punished ----".formatted(posts.size()));
        for (Post post : posts) {
            Integer punishScoreAmount = postScoreReducer.calculateReduceAmount(post);
            post.punishScore(punishScoreAmount);
            postRepository.save(post);
        }
        logger.info("--- %d posts are successfully punished ----".formatted(posts.size()));
    }
}
