package com.zimaberlin.zimasocial.entity.media;

import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;


@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookMediaJpa {
    @Column(name = "book_resource_id")
    private String id;
    @Column(name = "book_title", length = 64)
    private String title;
    @Column(name = "book_summary", length = 256)
    private String summary;
    @Column(name = "book_description")
    private String description;
    @Column(name = "author_name")
    private String author;
    @Column(name = "page_count")
    private Integer pageCount;
    @Column(name = "book_cover_url")
    private String thumbnail;
    @Column(name = "book_small_cover_url")
    private String smallThumbnail;
    @Enumerated(EnumType.STRING)
    @Column(name = "book_provider")
    private BookProvider provider;
    @Column(name = "book_publisher")
    private String publisher;
    @Column(name = "book_language")
    private String language;
    @Column(name = "book_publish_date")
    private String publishDate;
    @Column(name = "print_type")
    private String printType;

    public BookMediaJpa(BookMedia book) {
        this.id = book.getResourceId();
        this.title = book.getTitle();
        this.summary = book.getSummary();
        this.description = book.getDescription();
        this.author = book.getAuthor();
        this.pageCount = book.getPageCount();
        this.thumbnail = book.getThumbnail();
        this.smallThumbnail = book.getSmallThumbnail();
        this.provider = book.getProvider();
        this.publisher = book.getPublisher();
        this.language = book.getLanguage();
        this.publishDate = book.getPublishDate();
        this.printType = book.getPrintType();
    }
}
