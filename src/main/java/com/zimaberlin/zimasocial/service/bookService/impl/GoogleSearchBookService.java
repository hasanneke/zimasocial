package com.zimaberlin.zimasocial.service.bookService.impl;

import com.zimaberlin.zimasocial.service.bookService.domain.BookResponseView;
import com.zimaberlin.zimasocial.service.bookService.domain.SearchBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleSearchBookService implements SearchBookService {
    private final RestTemplate restTemplate;
    private String baseUrl;

    @Autowired
    public GoogleSearchBookService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://www.googleapis.com";
    }

    public GoogleBookSearchResult searchGoogleBook(String query){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(String.format("%s/books/v1/volumes", baseUrl))
                .queryParam("q", query);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<GoogleBookSearchResult> request = new HttpEntity<>(headers);
        // First get response as String to log it
        ResponseEntity<String> rawResponse = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                request,
                String.class
        );

         ResponseEntity<GoogleBookSearchResult> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                request,
                GoogleBookSearchResult.class);

        return response.getBody();
    }

    @Override
    public BookResponseView searchBook(String query) {
        GoogleBookSearchResult googleBooks = searchGoogleBook(query);
        BookResponseView bookResponseView = new BookResponseView();
        bookResponseView.setTotalCount(googleBooks.getTotalItems());
        bookResponseView.setItems(googleBooks.getItems().stream().map((e)->{

            BookResponseView.Book book = new BookResponseView.Book();
            book.setId(e.getId());
            book.setAuthor(e.getVolumeInfo().getAuthors().getFirst());
            book.setDescription(e.getVolumeInfo().getDescription());
            book.setPublisher(e.getVolumeInfo().getPublisher());
            book.setPublishedDate(e.getVolumeInfo().getPublishedDate());
            book.setPageCount(e.getVolumeInfo().getPageCount());
            book.setPrintType(e.getVolumeInfo().getPrintType());
            book.setTitle(e.getVolumeInfo().getTitle());

            if(e.getVolumeInfo().getImageLinks() != null){
                book.setThumbnail(e.getVolumeInfo().getImageLinks().getThumbnail());
            }
            book.setPreviewLink(e.getVolumeInfo().getPreviewLink());
            book.setSelfUrl(e.getSelfLink());

            return book;
        }).toList());

        return bookResponseView;
    }
}
