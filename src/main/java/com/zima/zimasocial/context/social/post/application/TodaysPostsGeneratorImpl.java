package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.author.abstracted.TodaysPostGenerator;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.entity.TodaysPost;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import com.zima.zimasocial.context.social.post.repository.TodaysPostRepository;
import com.zima.zimasocial.context.social.media.value.MediaType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TodaysPostsGeneratorImpl implements TodaysPostGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TodaysPostsGeneratorImpl.class);
    private final PostRepository postRepository;
    private final TodaysPostRepository todaysPostRepository;
    @Override
    public void createTodaysPost() {
        logger.info("Todays Posts Generator Started...");
        List<TodaysPost> currentTodaysPosts = todaysPostRepository.findAllByDate(LocalDate.now().minusDays(1));
        if(!currentTodaysPosts.isEmpty()){
            currentTodaysPosts = currentTodaysPosts.stream().peek(TodaysPost::setAsDeleted).toList();
            todaysPostRepository.saveAll(currentTodaysPosts);
        }
        List<TodaysPost> todaysPosts = selectTodaysPosts();
        todaysPostRepository.saveAll(todaysPosts);
        logger.info("Todays Posts Generator Ended.");
    }

    @Override
    public List<TodaysPost> selectTodaysPosts() {
        List<Post> yesterdaySharedPosts = postRepository.findAllByCreatedAtBetweenQuery(
                LocalDate.now().minusDays(1).atStartOfDay(),
                LocalDate.now().atStartOfDay()
        );
        List<Post> musics = yesterdaySharedPosts.stream().filter(e->e.getContent().getType().equals(MediaType.music))
                .sorted(Comparator.comparing((Post post)->post.getStats().baseScore())).toList().reversed();
        List<Post> movies = yesterdaySharedPosts.stream().filter(e->e.getContent().getType().equals(MediaType.movie))
                .sorted(Comparator.comparing((Post post) -> post.getStats().baseScore())).toList().reversed();
        List<Post> series = yesterdaySharedPosts.stream().filter(e->e.getContent().getType().equals(MediaType.tv))
                .sorted(Comparator.comparing((Post post) -> post.getStats().baseScore())).toList().reversed();
        List<Post> books = yesterdaySharedPosts.stream().filter(e->e.getContent().getType().equals(MediaType.book))
                .sorted(Comparator.comparing((Post post)->post.getStats().baseScore())).toList().reversed();

        Post todaysMusic = musics.stream().findFirst().orElse(null);
        Post todaysMovie = movies.stream().findFirst().orElse(null);
        Post todaysBook = books.stream().findFirst().orElse(null);
        Post todaysSeries = series.stream().findFirst().orElse(null);

        List<Post> selectedPosts = Stream.of(todaysMusic, todaysMovie, todaysBook, todaysSeries).toList().stream().filter(Objects::nonNull).toList();

        List<TodaysPost> todaysPosts = selectedPosts.stream().map(e-> TodaysPost.builder()
                        .post(e)
                        .authorId(e.getAuthorId())
                        .isDeleted(false)
                        .date(LocalDate.now().minusDays(1))
                        .build())
                .toList();

        if(todaysMusic != null){
            logger.debug(String.format("Todays music post id: %d, author: %s", todaysMusic.getId().getValue(), todaysMusic.getAuthorId()));
        }else{
            logger.error("Todays music not found");
        }
        if(todaysMovie != null){
            logger.debug(String.format("Todays Movie Post id: %d, author: %s", todaysMovie.getId().getValue(), todaysMovie.getAuthorId()));
        }else{
            logger.error("Todays movie post not found");
        }
        if(todaysBook != null){
            logger.debug(String.format("Todays Book Post id: %d, author: %s", todaysBook.getId().getValue(), todaysBook.getAuthorId()));
        }else{
            logger.error("Todays book post not found");
        }
        if(todaysSeries != null){
            logger.debug(String.format("Todays Series Post id: %d, author: %s", todaysSeries.getId().getValue(), todaysSeries.getAuthorId()));
        }else{
            logger.error("Todays series post not found");
        }        return todaysPosts;
    }
}
