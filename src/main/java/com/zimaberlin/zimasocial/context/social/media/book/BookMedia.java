package com.zimaberlin.zimasocial.context.social.media.book;

import com.zimaberlin.zimasocial.entity.media.BookProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class BookMedia {
    private UUID id;
    private String resourceId;
    private Long postId;
    private String description;
    private String summary;
    private String title;
    private String author;
    private String publisher;
    private String thumbnail;
    private String smallThumbnail;
    private String publishDate;
    private String printType;
    private Integer pageCount;
    private String language;
    private BookProvider provider;

    public BookMedia(UUID id, SearchBookMediaItem searchBookMediaItem) {
        this.id = id;
        this.resourceId = searchBookMediaItem.getId();
        this.description = searchBookMediaItem.getDescription();
        this.title = searchBookMediaItem.getTitle();
        this.author = searchBookMediaItem.getAuthor();
        this.publisher = searchBookMediaItem.getPublisher();
        this.thumbnail = searchBookMediaItem.getThumbnail();
        this.smallThumbnail = searchBookMediaItem.getSmallThumbnail();
        this.publishDate = searchBookMediaItem.getPublishDate();
        this.printType = searchBookMediaItem.getPrintType();
        this.pageCount = searchBookMediaItem.getPageCount();
        this.language = searchBookMediaItem.getLanguage();
        this.provider = searchBookMediaItem.getProvider();
        this.summary = searchBookMediaItem.getDescription();
    }
    public void assignId(UUID id){
        this.id = id;
    }
}
