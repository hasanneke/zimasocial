package com.zimaberlin.zimasocial.service.bookService.domain;

public interface SearchBookService {
     BookResponseView searchBook(String query);
     BookResponseView.Book getBook(String id);
}
