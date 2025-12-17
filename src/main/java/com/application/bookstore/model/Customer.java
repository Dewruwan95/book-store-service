package com.application.bookstore.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name",length = 20,nullable = false)
    private String firstName;

    @Column(name = "last_name",length = 20,nullable = false)
    private String lastName;


    @Column(length = 30,nullable = false)
    private String email;

    @Column(length = 20,nullable = false)
    private String phoneNumber;

    @Column(length = 50,nullable = false)
    private String address;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<PurchasedBook> purchasedBooks;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PurchasedBook> getPurchasedBooks() {
        return purchasedBooks;
    }

    public void setPurchasedBooks(List<PurchasedBook> purchasedBooks) {
        this.purchasedBooks = purchasedBooks;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", purchasedBooks=" + purchasedBooks +
                '}';
    }
}
