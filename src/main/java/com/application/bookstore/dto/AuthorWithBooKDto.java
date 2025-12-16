package com.application.bookstore.dto;

public class AuthorWithBooKDto {
    private AuthorDto authorDto;

    private BookRequestDto bookRequestDto;

    public AuthorDto getAuthorDto() {
        return authorDto;
    }

    public void setAuthorDto(AuthorDto authorDto) {
        this.authorDto = authorDto;
    }

    public BookRequestDto getBookRequestDto() {
        return bookRequestDto;
    }

    public void setBookRequestDto(BookRequestDto bookRequestDto) {
        this.bookRequestDto = bookRequestDto;
    }
}