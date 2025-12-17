package com.application.bookstore.dto;


import java.util.List;

public class BookDto {

    private int id;
    private String title;
    private  double price;
    private String genre;
    private int stock;

    private List<AuthorRequestDto> author;

    public List<AuthorRequestDto> getAuthor() {
        return author;
    }

    public void setAuthor(List<AuthorRequestDto> author) {
        this.author = author;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}