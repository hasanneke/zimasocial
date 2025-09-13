package com.zimaberlin.zimasocial.context.social.media.book;

import com.zimaberlin.zimasocial.entity.media.BookProvider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
public class SearchBookMediaItem {
    private String id;
    private String description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchBookMediaItem that = (SearchBookMediaItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
