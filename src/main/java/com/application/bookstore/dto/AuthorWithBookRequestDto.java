package com.application.bookstore.dto;

import java.util.List;

public class AuthorWithBookRequestDto {
    private AuthorRequestDto author;
    private List<Integer> bookIds;

    public AuthorRequestDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorRequestDto author) {
        this.author = author;
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Integer> bookIds) {
        this.bookIds = bookIds;
    }

}