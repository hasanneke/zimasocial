package com.zimaberlin.zimasocial.context.social.media.infastructure.book;

import com.zimaberlin.zimasocial.context.social.media.MediaCollection;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.BookSearcher;
import com.zimaberlin.zimasocial.context.social.media.book.SearchBookMediaItem;
import com.zimaberlin.zimasocial.entity.media.BookProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GoogleBookSearcher implements BookSearcher {
    private final RestTemplate restTemplate;
    private String baseUrl;
    private MediaCollection mediaRepository;

    @Autowired
    public GoogleBookSearcher(RestTemplateBuilder restTemplateBuilder, MediaCollection mediaRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://www.googleapis.com";
        this.mediaRepository = mediaRepository;
    }

    @Override
    public List<SearchBookMediaItem> search(String query) {
        BookResponseView responseView = this.searchGoogleBooks(query);
        return responseView.getItems().stream().map(e-> SearchBookMediaItem
                .builder()
                .id(e.getId())
                .author(e.getAuthor())
                .description(e.getDescription())
                .publisher(e.getPublisher())
                .title(e.getTitle())
                .publishDate(e.getPublishedDate())
                .thumbnail(e.getThumbnail())
                .pageCount(e.getPageCount())
                .printType(e.getPrintType())
                .smallThumbnail(e.getSmallThumbnail())
                .thumbnail(e.getThumbnail())
                .provider(responseView.getProvider())
                .build()).toList();
    }

    @Override
    public Optional<BookMedia> getBook(String id) {
        BookResponseView.Book book = this.getGoogleBook(id);
        return Optional.ofNullable(map(book));
    }

    private BookResponseView.Book createBookViewFromGoogleResultBook(GoogleBookSearchResult.Book e) {
        BookResponseView.Book book = new BookResponseView.Book();
        book.setId(e.getId());
        book.setAuthor(e.getVolumeInfo().getAuthors().isEmpty() ? "" : e.getVolumeInfo().getAuthors().getFirst());
        book.setDescription(e.getVolumeInfo().getDescription());
        book.setPublisher(e.getVolumeInfo().getPublisher());
        book.setPublishedDate(e.getVolumeInfo().getPublishedDate());
        book.setPageCount(e.getVolumeInfo().getPageCount());
        book.setPrintType(e.getVolumeInfo().getPrintType());
        book.setTitle(e.getVolumeInfo().getTitle());

        if(e.getVolumeInfo().getImageLinks() != null){
            book.setSmallThumbnail(e.getVolumeInfo().getImageLinks().getSmallThumbnail());
            book.setThumbnail(e.getVolumeInfo().getImageLinks().getThumbnail());
        }
        book.setPreviewLink(e.getVolumeInfo().getPreviewLink());
        book.setSelfUrl(e.getSelfLink());

        return book;
    }
    private BookResponseView.Book getGoogleBook(String id) {
        GoogleBookSearchResult.Book book = restTemplate
                .getForObject(String.format("%s/books/v1/volumes/%s", baseUrl, id),
                        GoogleBookSearchResult.Book.class);
        if(book != null){
            return createBookViewFromGoogleResultBook(book);
        }else{
            return null;
        }
    }

    private BookMedia map(BookResponseView.Book e) {
        if(e != null){
            return BookMedia
                    .builder()
                    .id(mediaRepository.nextId())
                    .resourceId(e.getId())
                    .author(e.getAuthor())
                    .description(e.getDescription())
                    .publisher(e.getPublisher())
                    .title(e.getTitle())
                    .publishDate(e.getPublishedDate())
                    .thumbnail(e.getThumbnail())
                    .pageCount(e.getPageCount())
                    .printType(e.getPrintType())
                    .smallThumbnail(e.getSmallThumbnail())
                    .thumbnail(e.getThumbnail())
                    .build();
        }else{
            return null;
        }

    }

    private BookResponseView searchGoogleBooks(String query) {
        GoogleBookSearchResult googleBooks = searchGoogleBook(query);
        BookResponseView bookResponseView = new BookResponseView();
        bookResponseView.setTotalCount(googleBooks.getTotalItems());
        bookResponseView.setItems(googleBooks.getItems().stream().map(this::createBookViewFromGoogleResultBook).toList());
        bookResponseView.setProvider(BookProvider.google);
        return bookResponseView;
    }

    private GoogleBookSearchResult searchGoogleBook(String query){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(String.format("%s/books/v1/volumes", baseUrl))
                .encode(StandardCharsets.UTF_8)
                .queryParam("q", query)
                .build()
                .toUri();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<GoogleBookSearchResult> request = new HttpEntity<>(headers);
        ResponseEntity<GoogleBookSearchResult> response = restTemplate.exchange(uriComponentsBuilder,
                HttpMethod.GET,
                request,
                GoogleBookSearchResult.class);

        return response.getBody();
    }
}
