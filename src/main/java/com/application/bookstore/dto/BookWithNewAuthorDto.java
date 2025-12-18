package com.application.bookstore.dto;

public class BookWithNewAuthorDto {

    private BookRequestDto book;

    private AuthorRequestDto author;


    public BookRequestDto getBook() {
        return book;
    }

    public void setBook(BookRequestDto book) {
        this.book = book;
    }

    public AuthorRequestDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorRequestDto author) {
        this.author = author;
    }
}