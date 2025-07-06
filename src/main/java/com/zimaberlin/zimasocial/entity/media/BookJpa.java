package com.zimaberlin.zimasocial.entity.media;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class BookJpa {
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
    private String coverUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "book_provider")
    private BookProvider provider;
}
