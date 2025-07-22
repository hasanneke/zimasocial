package com.zimaberlin.zimasocial.context.social.media;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class BookMedia {
    private String id;
    private String description;
    private String title;
    private String author;
    private String publisher;
    private String thumbnail;
    private String smallThumbnail;
    private LocalDate publishDate;
    private String printType;
    private Integer pageCount;
    private String language;
}
