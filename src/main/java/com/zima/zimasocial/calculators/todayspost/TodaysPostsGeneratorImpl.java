package com.zima.zimasocial.calculators.todayspost;

import com.zima.zimasocial.calculators.PostScoreCalculator;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.entity.PostEntity;
import com.zima.zimasocial.entity.todayspost.TodaysPost;
import com.zima.zimasocial.repository.PostJpaRepository;
import com.zima.zimasocial.repository.TodaysPostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TodaysPostsGeneratorImpl implements TodaysPostGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TodaysPostsGeneratorImpl.class);
    private final PostJpaRepository postJpaRepository;
    private final PostScoreCalculator postScoreCalculator;
    private final TodaysPostRepository todaysPostRepository;
    @Override
    public void createTodaysPost() {
        logger.info("Todays Posts Generator Started...");
        List<TodaysPost> currentTodaysPosts = todaysPostRepository.findTodaysPostByDate(LocalDate.now().minusDays(1));
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
        List<PostEntity> yesterdaySharedPosts = postJpaRepository.findAllByCreatedAtBetween(
                LocalDate.now().minusDays(1).atStartOfDay(),
                LocalDate.now().atStartOfDay()
        );
        List<PostEntity> musics = yesterdaySharedPosts.stream().filter(e->e.getType().equals(MediaType.music))
                .sorted(Comparator.comparing(postScoreCalculator::calculateScore)).toList().reversed();
        List<PostEntity> movies = yesterdaySharedPosts.stream().filter(e->e.getType().equals(MediaType.movie))
                .sorted(Comparator.comparing(postScoreCalculator::calculateScore)).toList().reversed();
        List<PostEntity> books = yesterdaySharedPosts.stream().filter(e->e.getType().equals(MediaType.book))
                .sorted(Comparator.comparing(postScoreCalculator::calculateScore)).toList().reversed();

        PostEntity todaysMusic = musics.stream().findFirst().orElse(null);
        PostEntity todaysMovie = movies.stream().findFirst().orElse(null);
        PostEntity todaysBook = books.stream().findFirst().orElse(null);

        List<PostEntity> selectedPosts = Stream.of(todaysMusic, todaysMovie, todaysBook).toList().stream().filter(e->e != null).toList();

        List<TodaysPost> todaysPosts = selectedPosts.stream().map(e-> TodaysPost.builder()
                        .post(e)
                        .author(e.getUser())
                        .isDeleted(false)
                        .date(LocalDate.now().minusDays(1))
                        .build())
                .toList();

        if(todaysMusic != null){
            logger.debug(String.format("Todays music post id: %d, author: %s", todaysMusic.getId(), todaysMusic.getUser().getSlug()));
        }else{
            logger.error("Todays music not found");
        }
        if(todaysMovie != null){
            logger.debug(String.format("Todays Movie Post id: %d, author: %s", todaysMovie.getId(), todaysMovie.getUser().getSlug()));
        }else{
            logger.error("Todays movie post not found");
        }
        if(todaysBook != null){
            logger.debug(String.format("Todays Book Post id: %d, author: %s", todaysBook.getId(), todaysBook.getUser().getSlug()));
        }else{
            logger.error("Todays book post not found");
        }
        return todaysPosts;
    }
}
