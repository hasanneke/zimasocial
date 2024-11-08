package com.zimaberlin.zimasocial.service.bookService.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.util.Date;
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
        private String selfUrl;
        private String publishedDate;
        private String previewLink;
        private String printType;
        private int pageCount;
    }
}
