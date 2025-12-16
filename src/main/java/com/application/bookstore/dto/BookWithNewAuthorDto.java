package com.application.bookstore.dto;

public class BookWithNewAuthorDto {

    private BookRequestDto book;

    private AuthorDto author;

    public BookRequestDto getBook() {
        return book;
    }

    public void setBook(BookRequestDto book) {
        this.book = book;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }
}