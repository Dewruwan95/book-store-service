package com.application.bookstore.dto;

import java.util.List;

public class AuthorDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String nationality;
    private List<BookRequestDto> bookIds;

    public List<BookRequestDto> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<BookRequestDto> bookIds) {
        this.bookIds = bookIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}