package com.application.bookstore.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "purchased_books")
public class PurchasedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne//many purchased entities belong one book
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate purchaseDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "PurchasedBook{" +
                "id=" + id +
                ", customer=" + customer +
                ", book=" + book +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}