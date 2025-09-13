package com.zimaberlin.zimasocial.context.social.media.infastructure.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class GoogleBookSearchResult {
    @JsonProperty("totalItems")
    private int totalItems;
    private List<Book> items = List.of();
    @Getter
    @Setter
    public static class Book {
        private String id;
        @JsonProperty("selfLink")
        private String selfLink;
        @JsonProperty("volumeInfo")
        private BookVolumeInfo volumeInfo;
        @Getter
        @Setter
        public static class BookVolumeInfo {
            @JsonProperty("title")
            private String title;
            @JsonProperty("authors")
            private List<String> authors = List.of();
            @JsonProperty("publisher")
            private String publisher;
            @JsonProperty("pageCount")
            private int pageCount;
            @JsonProperty("printType")
            private String printType;
            @JsonProperty("imageLinks")
            private BookImageLink imageLinks;
            @JsonProperty("description")
            private String description;
            @JsonProperty("publishedDate")
            private String publishedDate;
            @JsonProperty("previewLink")
            private String previewLink;
            @JsonProperty("language")
            private String language;
            @Getter
            @Setter
            public static class BookImageLink {
                @JsonProperty("smallThumbnail")
                private String smallThumbnail;
                private String thumbnail;
            }
        }
    }
}
