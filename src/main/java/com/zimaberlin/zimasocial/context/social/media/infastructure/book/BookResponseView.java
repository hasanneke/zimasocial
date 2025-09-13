package com.zimaberlin.zimasocial.context.social.media.infastructure.book;

import com.zimaberlin.zimasocial.entity.media.BookProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseView {
    private int totalCount;
    private List<Book> items = List.of();
    @Getter
    @Setter
    public static class Book {
        private String id;
        private String description;
        private String title;
        private String author;
        private String publisher;
        private String thumbnail;
        private String smallThumbnail;
        private String selfUrl;
        private String publishedDate;
        private String previewLink;
        private String printType;
        private int pageCount;
    }
    private BookProvider provider;
}
