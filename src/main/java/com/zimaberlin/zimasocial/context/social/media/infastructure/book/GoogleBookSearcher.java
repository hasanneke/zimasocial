package com.zimaberlin.zimasocial.context.social.media.infastructure.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import com.zimaberlin.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zimaberlin.zimasocial.context.social.media.BookSearcher;
import com.zimaberlin.zimasocial.context.social.media.book.SearchBookMediaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GoogleBookSearcher implements BookSearcher {
    private final RestTemplate restTemplate;
    private String baseUrl;
    private MediaItemJpaRepository mediaItemJpaRepository;

    @Autowired
    public GoogleBookSearcher(RestTemplateBuilder restTemplateBuilder, MediaItemJpaRepository mediaItemJpaRepository, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://www.googleapis.com";
        this.mediaItemJpaRepository = mediaItemJpaRepository;
    }

    @Override
    public List<SearchBookMediaItem> search(String query) {
        return List.of();
    }

    @Override
    @Transactional
    public MediaItem getBook(String id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = String.format("%s/books/v1/volumes/%s", baseUrl, id);
        GoogleBookSearchResult.Book book = restTemplate
                .getForObject(String.format("%s/books/v1/volumes/%s", baseUrl, id),
                        GoogleBookSearchResult.Book.class);
        MediaItem mediaItem = new MediaItem();
        mediaItem.setProvider("google");
        mediaItem.setResourceUrl(url);
        mediaItem.setResourceId(id);
        mediaItem.setContent(objectMapper.writeValueAsString(book));
        return mediaItem;
    }
}
